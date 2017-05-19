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

    private final FrameBufferBitmap odBitmap = new FrameBufferBitmap();
    private final FrameBufferBitmap lmBitmap = new FrameBufferBitmap();

    private MapPosition lmMapPosition;

    private Dimension dimension;
    private final DisplayModel displayModel;
    private final FrameBufferModel frameBufferModel;
    private final Matrix matrix;

    private final Object dimLock = new Object();

    public FrameBufferHack(Model model) {
        super(model.frameBufferModel, model.displayModel, AndroidGraphicFactory.INSTANCE);
        this.frameBufferModel = model.frameBufferModel;
        this.displayModel = model.displayModel;
        GraphicFactory graphicFactory = AndroidGraphicFactory.INSTANCE;
        this.matrix = graphicFactory.createMatrix();
    }


    // this is called from MapView.onDraw()
    public void draw(GraphicContext graphicContext) {


        graphicContext.fillColor(this.displayModel.getBackgroundColor());

        //odBitmap.release(); // now we allow swap because we asume that the last drawing is finished

        swapBitmaps();

        Bitmap b = odBitmap.lock(); // lock next frame (start drawing again)
        if (b != null) {
            synchronized(dimLock) {
                graphicContext.drawBitmap(b, this.matrix);
            }
        }
        odBitmap.release();
    }


    private void swapBitmaps() {
        /*
          Swap bitmaps only if the layerManager is currently not working and
          has drawn a new bitmap since the last swap
         */
        if (FrameBufferBitmap.swap(odBitmap, lmBitmap)) {
            frameBufferModel.setMapPosition(lmMapPosition);
        }
    }


    // this is called from layer manager when drawing starts
    public Bitmap getDrawingBitmap() {

        Bitmap b = lmBitmap.lockWhenSwapped();

        if (b != null) {
            b.setBackgroundColor(this.displayModel.getBackgroundColor());
        }

        return b;
    }


    // this is called from layer manager when drawing is finished
    public void frameFinished(MapPosition framePosition) {
        lmBitmap.release(); // allow swap
        lmMapPosition = framePosition;
    }



    public void adjustMatrix(float diffX, float diffY, float scaleFactor, Dimension mapViewDimension,
                                          float pivotDistanceX, float pivotDistanceY) {

        synchronized(dimLock) {

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

    public synchronized void destroy() {

        destroyBitmaps();
    }






    public Dimension getDimension() {

        return this.dimension;
    }


    public void setDimension(Dimension dimension) {

        synchronized (dimLock) {
            if (this.dimension != null && this.dimension.equals(dimension)) {
                return;
            }
            this.dimension = dimension;

            destroyBitmaps();

            if (dimension.width > 0 && dimension.height > 0) {
                odBitmap.create(dimension.width, dimension.height);
                lmBitmap.create(dimension.width, dimension.height);
            }
        }
    }

    private void centerFrameBufferToMapView(Dimension mapViewDimension) {
        float dx = (this.dimension.width - mapViewDimension.width) / -2f;
        float dy = (this.dimension.height - mapViewDimension.height) / -2f;
        this.matrix.translate(dx, dy);
    }


    private void destroyBitmaps() {

       odBitmap.destroy();
       lmBitmap.destroy();
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
