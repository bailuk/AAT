//package ch.bailu.aat.map.mapsforge;
//
//import org.mapsforge.core.graphics.Bitmap;
//import org.mapsforge.core.graphics.Canvas;
//import org.mapsforge.core.graphics.GraphicFactory;
//import org.mapsforge.core.model.BoundingBox;
//import org.mapsforge.core.model.Dimension;
//import org.mapsforge.core.model.MapPosition;
//import org.mapsforge.core.model.Point;
//import org.mapsforge.map.layer.Layer;
//import org.mapsforge.map.layer.Layers;
//import org.mapsforge.map.layer.Redrawer;
//import org.mapsforge.map.model.MapViewPosition;
//import org.mapsforge.map.util.MapPositionUtil;
//import org.mapsforge.map.util.PausableThread;
//import org.mapsforge.map.view.FrameBuffer;
//import org.mapsforge.map.view.MapView;
//
//public class MiniLayerManager  implements Redrawer {
//    private static final int MILLISECONDS_PER_FRAME = 30;
//
//    private final Canvas drawingCanvas;
//    private final Layers layers;
//    private final MapView mapView;
//    private final MapViewPosition mapViewPosition;
//    private boolean redrawNeeded;
//
//
//    public MiniLayerManager(MapView mapView, MapViewPosition mapViewPosition, GraphicFactory graphicFactory) {
//        super();
//
//        this.mapView = mapView;
//        this.mapViewPosition = mapViewPosition;
//
//        this.drawingCanvas = graphicFactory.createCanvas();
//
//
//        this.layers = new Layers(this, mapView.getModel().displayModel);
//    }
//
//    public Layers getLayers() {
//        return this.layers;
//    }
//
//    @Override
//    public void redrawLayers() {
//        this.redrawNeeded = true;
//        synchronized (this) {
//            notify();
//        }
//    }
//
//    @Override
//    protected void afterRun() {
//        synchronized (this.layers) {
//            for (Layer layer : this.layers) {
//                layer.onDestroy();
//            }
//        }
//        this.drawingCanvas.destroy();
//    }
//
//    @Override
//    protected void doWork() throws InterruptedException {
//        long startTime = System.nanoTime();
//        this.redrawNeeded = false;
//
//        FrameBuffer frameBuffer = this.mapView.getFrameBuffer();
//        Bitmap bitmap = frameBuffer.getDrawingBitmap();
//        if (bitmap != null) {
//            this.drawingCanvas.setBitmap(bitmap);
//
//            MapPosition mapPosition = this.mapViewPosition.getMapPosition();
//            Dimension canvasDimension = this.drawingCanvas.getDimension();
//            int tileSize = this.mapView.getModel().displayModel.getTileSize();
//            BoundingBox boundingBox = MapPositionUtil.getBoundingBox(mapPosition, canvasDimension, tileSize);
//            Point topLeftPoint = MapPositionUtil.getTopLeftPoint(mapPosition, canvasDimension, tileSize);
//
//            synchronized (this.layers) {
//                for (Layer layer : this.layers) {
//                    if (layer.isVisible()) {
//                        layer.drawInside(boundingBox, mapPosition.zoomLevel, this.drawingCanvas, topLeftPoint);
//                    }
//                }
//            }
//
//            if (!mapViewPosition.animationInProgress()) {
//                // this causes a lot of flickering when an animation
//                // is in progress
//                frameBuffer.frameFinished(mapPosition);
//                this.mapView.repaint();
//            } else {
//                // make sure that we redraw at the end
//                this.redrawNeeded = true;
//            }
//        }
//
//        long elapsedMilliseconds = (System.nanoTime() - startTime) / 1000000;
//        long timeSleep = MILLISECONDS_PER_FRAME - elapsedMilliseconds;
//
//        if (timeSleep > 1 && !isInterrupted()) {
//            sleep(timeSleep);
//        }
//    }
//
//    @Override
//    protected ThreadPriority getThreadPriority() {
//        return ThreadPriority.NORMAL;
//    }
//
//    @Override
//    protected boolean hasWork() {
//        return this.redrawNeeded;
//    }
//}