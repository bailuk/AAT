package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.FrameBufferModel;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.view.FrameBuffer;

public class FrameBufferHack extends FrameBuffer {
    private static final boolean IS_TRANSPARENT = false;

    /**
     * odBitmap: onDraw bitmap  -
     *           the bitmap that gets displayed by the MapView.onDraw() function from the UI thread
     * lmBitmap: layerManager bitmap -
     *           the bitmap that gets drawn by the LayerManager.doWork() thread
     */


    private Bitmap odBitmap;
    private Bitmap lmBitmap;
    private MapPosition lmMapPosition;

    private final Object lmBitmapLock = new Object();

    private boolean allowBitmapSwap = true;


    private Dimension dimension;
    private final DisplayModel displayModel;
    private final FrameBufferModel frameBufferModel;
    private final GraphicFactory graphicFactory;
    private final Matrix matrix;

    public FrameBufferHack(Model model) {
        super(model.frameBufferModel, model.displayModel, AndroidGraphicFactory.INSTANCE);
        this.frameBufferModel = model.frameBufferModel;
        this.displayModel = model.displayModel;
        this.graphicFactory = AndroidGraphicFactory.INSTANCE;
        this.matrix = graphicFactory.createMatrix();
    }


    // this is called from MapView.onDraw()
    public void draw(GraphicContext graphicContext) {

        graphicContext.fillColor(this.displayModel.getBackgroundColor());

        /**
         * swap bitmaps right before the canvas.drawBitmap function to prevent flickering as much
         * as possible
         */
        swapBitmaps();
        if (this.odBitmap != null) {
            graphicContext.drawBitmap(this.odBitmap, this.matrix);
        }
    }


    private void swapBitmaps() {
        synchronized (lmBitmapLock) {
            /**
             * Swap bitmaps only if the layerManager is currently not working and
             * has drawn a new bitmap since the last swap
             */
            if (allowBitmapSwap) {
                Bitmap bitmapTemp = this.odBitmap;
                this.odBitmap = this.lmBitmap;
                this.lmBitmap = bitmapTemp;


                this.frameBufferModel.setMapPosition(lmMapPosition);

                allowBitmapSwap = false;
                lmBitmapLock.notify();
            }
        }
    }


    // this is called from layer manager when drawing starts
    public Bitmap getDrawingBitmap() {

        lockLmBitmap();


        if (this.lmBitmap != null) {
            this.lmBitmap.setBackgroundColor(this.displayModel.getBackgroundColor());
        }

        return this.lmBitmap;
    }


    private void lockLmBitmap() {
        synchronized (lmBitmapLock) {
            if (this.lmBitmap != null) {
                if (allowBitmapSwap) { // not yet swapped by onDraw()
                    try {
                        lmBitmapLock.wait(); // wait until swapped
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            allowBitmapSwap = false;
        }
    }

    // this is called from layer manager when drawing is finished
    public void frameFinished(MapPosition frameMapPosition) {
        freeLmBitmap(frameMapPosition);
    }


    private void freeLmBitmap(MapPosition frameMapPosition) {
        synchronized (lmBitmapLock) {
            lmMapPosition = frameMapPosition;
            allowBitmapSwap = true;
        }
    }




     public synchronized void adjustMatrix(float diffX, float diffY, float scaleFactor, Dimension mapViewDimension,
                                          float pivotDistanceX, float pivotDistanceY) {
        if (this.dimension == null) {
            return;
        }
        this.matrix.reset();
        centerFrameBufferToMapView(mapViewDimension);
        if (pivotDistanceX == 0 && pivotDistanceY == 0) {
            // only translate the matrix if we are not zooming around a pivot,
            // the translation happens only once the zoom is finished.
            this.matrix.translate(diffX, diffY);
        }

        scale(scaleFactor, pivotDistanceX, pivotDistanceY);
    }

    public synchronized void destroy() {
        destroyBitmaps();
    }






    public synchronized Dimension getDimension() {
        return this.dimension;
    }


    public synchronized void setDimension(Dimension dimension) {
        if (this.dimension != null && this.dimension.equals(dimension)) {
            return;
        }
        this.dimension = dimension;

        destroyBitmaps();

        if (dimension.width > 0 && dimension.height > 0) {
            this.odBitmap = this.graphicFactory.createBitmap(dimension.width, dimension.height, IS_TRANSPARENT);
            this.lmBitmap = this.graphicFactory.createBitmap(dimension.width, dimension.height, IS_TRANSPARENT);
        }
    }

    private void centerFrameBufferToMapView(Dimension mapViewDimension) {
        float dx = (this.dimension.width - mapViewDimension.width) / -2f;
        float dy = (this.dimension.height - mapViewDimension.height) / -2f;
        this.matrix.translate(dx, dy);
    }

    private void destroyBitmaps() {
        if (this.odBitmap != null) {
            this.odBitmap.decrementRefCount();
            this.odBitmap = null;
        }
        if (this.lmBitmap != null) {
            this.lmBitmap.decrementRefCount();
            this.lmBitmap = null;
        }
    }

    private void scale(float scaleFactor, float pivotDistanceX, float pivotDistanceY) {
        if (scaleFactor != 1) {
            final Point center = this.dimension.getCenter();
            float pivotX = (float) (pivotDistanceX + center.x);
            float pivotY = (float) (pivotDistanceY + center.y);
            this.matrix.scale(scaleFactor, scaleFactor, pivotX, pivotY);
        }
    }

}
