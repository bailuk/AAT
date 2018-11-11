
package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicContext;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.Matrix;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.FrameBufferModel;
import org.mapsforge.map.view.FrameBuffer;

public class FrameBufferHack extends FrameBuffer {


    private static final boolean IS_TRANSPARENT = false;

    /*
     *  lm: layer manager
            Layer manager draws the draw off-screen
     *  od: onDraw() -> draw()
     *      swaps the two bitmaps and puts one draw to the screen
     *      while the layer manager draws the takeAndProcess off-screen draw.
     *
     */

    private final FrameBufferBitmap odBitmap = new FrameBufferBitmap();
    private final FrameBufferBitmap lmBitmap = new FrameBufferBitmap();
    private MapPosition lmMapPosition;

    private final FrameBufferBitmap.Lock allowSwap = new FrameBufferBitmap.Lock();

    private Dimension dimension;
    private final Matrix matrix;

    private final DisplayModel displayModel;
    private final FrameBufferModel frameBufferModel;
    private final GraphicFactory graphicFactory;


    public FrameBufferHack(FrameBufferModel frameBufferModel, DisplayModel displayModel,
                           GraphicFactory graphicFactory) {
        super(frameBufferModel, displayModel, graphicFactory);

        this.frameBufferModel = frameBufferModel;
        this.displayModel = displayModel;

        this.graphicFactory = graphicFactory;
        this.matrix = graphicFactory.createMatrix();

        allowSwap.disable();
    }


    /**
     * This is called from (Android) <code>MapView.onDraw()</code>.
     */
    public void draw(GraphicContext graphicContext) {


        /*
         * Swap bitmaps here (and only here).
         * Swapping is done when layer manager has finished. Else draw old draw again.
         * This (onDraw()) is allways called when layer manager has finished. This ensures that the
         * last generated frame is allways put on screen.
         */

        // FIXME: reseting the background color is redundant if the background color of the map view is allready set
        graphicContext.fillColor(this.displayModel.getBackgroundColor());

        swapBitmaps();

        synchronized(matrix) {

            Bitmap b = odBitmap.lock();
            if (b != null) {
                graphicContext.drawBitmap(b, this.matrix);
            }
        }

        /*
         * Release here so destroy() can free resources
         */
        this.odBitmap.release();
    }


    private void swapBitmaps() {
        /*
         *  Swap bitmaps only if the layerManager is currently not working and
         *  has drawn a new draw since the last swap
         */
        synchronized (allowSwap) {
            if (allowSwap.isEnabled()) {
                FrameBufferBitmap.swap(odBitmap, lmBitmap);
                frameBufferModel.setMapPosition(lmMapPosition);
                allowSwap.disable();
            }
        }
    }


    /**
     * This is called from <code>LayerManager</code> when drawing starts.
     * @return the draw of the second frame to draw on (may be null).
     */
    public Bitmap getDrawingBitmap() {
        /*
         * Layer manager only starts drawing a new draw when the last one is swapped (taken to
         * the screen). This ensures that the layer manager draws not too many frames. (only as
         * much as can get displayed).
         */

        synchronized (allowSwap) {
            allowSwap.waitDisabled();

            Bitmap b = lmBitmap.lock();

            if (b != null) {
                b.setBackgroundColor(this.displayModel.getBackgroundColor());
            }
            return b;
        }


    }


    /**
     * This is called from <code>LayerManager</code> when drawing is finished.
     */
    public void frameFinished(MapPosition framePosition) {
        synchronized(allowSwap) {
            lmMapPosition = framePosition;
            lmBitmap.release();

            allowSwap.enable();
        }
    }



    public void adjustMatrix(float diffX, float diffY, float scaleFactor, Dimension mapViewDimension,
                             float pivotDistanceX, float pivotDistanceY) {

        synchronized(matrix) {
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

    }


    private void centerFrameBufferToMapView(Dimension mapViewDimension) {
        float dx = (this.dimension.width - mapViewDimension.width) / -2f;
        float dy = (this.dimension.height - mapViewDimension.height) / -2f;
        this.matrix.translate(dx, dy);
    }


    private void scale(float scaleFactor, float pivotDistanceX, float pivotDistanceY) {
        if (scaleFactor != 1) {
            final Point center = this.dimension.getCenter();
            float pivotX = (float) (pivotDistanceX + center.x);
            float pivotY = (float) (pivotDistanceY + center.y);
            this.matrix.scale(scaleFactor, scaleFactor, pivotX, pivotY);
        }
    }


    public synchronized void destroy() {
        odBitmap.destroy();
        lmBitmap.destroy();
    }



    public Dimension getDimension() {
        return this.dimension;
    }


    public void setDimension(Dimension dimension) {
        synchronized(matrix) {
            if (this.dimension != null && this.dimension.equals(dimension)) {
                return;
            }
            this.dimension = dimension;
        }

        synchronized(allowSwap) {
            odBitmap.create(graphicFactory, dimension,
                    this.displayModel.getBackgroundColor(),
                    IS_TRANSPARENT);
            lmBitmap.create(graphicFactory, dimension,
                    this.displayModel.getBackgroundColor(),
                    IS_TRANSPARENT);
        }
    }
}
