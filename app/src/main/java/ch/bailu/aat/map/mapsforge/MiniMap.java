//package ch.bailu.aat.map.mapsforge;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import org.mapsforge.core.model.BoundingBox;
//import org.mapsforge.core.model.Dimension;
//import org.mapsforge.core.model.LatLong;
//import org.mapsforge.core.model.MapPosition;
//import org.mapsforge.core.util.LatLongUtils;
//import org.mapsforge.map.layer.Layer;
//import org.mapsforge.map.layer.LayerManager;
//import org.mapsforge.map.model.Model;
//import org.mapsforge.map.model.common.Observer;
//import org.mapsforge.map.scalebar.MapScaleBar;
//import org.mapsforge.map.util.MapPositionUtil;
//import org.mapsforge.map.util.MapViewProjection;
//import org.mapsforge.map.view.FpsCounter;
//import org.mapsforge.map.view.FrameBuffer;
//import org.mapsforge.map.view.MapView;
//
//import java.util.ArrayList;
//
//import ch.bailu.aat.coordinates.BoundingBoxE6;
//import ch.bailu.aat.map.MapContext;
//import ch.bailu.aat.map.MapViewInterface;
//import ch.bailu.aat.map.layer.MapLayerInterface;
//
//public class MiniMap extends ViewGroup implements MapView, MapViewInterface, Observer {
//
//    private final Model model = new Model();
//    private final MapViewProjection projection;
//
//
//    private final ArrayList<MapLayerInterface> layers = new ArrayList(10);
//
//
//    private final MapContext mcontext;
//
//
//    public MiniMap(Context context) {
//        super(context);
//        projection = new MapViewProjection(this);
//    }
//
//    @Override
//    protected void onLayout(boolean c, int l, int t, int r, int b) {
//        for (MapLayerInterface layer: layers) layer.onLayout(c,l,t,r,b);
//    }
//
//    @Override
//    public void frameBounding(BoundingBoxE6 boundingBox) {
//        frameBounding(boundingBox.toBoundingBox());
//    }
//
//
//    private void frameBounding(BoundingBox bounding) {
//        Dimension dimension = model.mapViewDimension.getDimension();
//        byte zoom = LatLongUtils.zoomForBounds(
//                dimension,
//                bounding,
//                getModel().displayModel.getTileSize());
//
//        MapPosition position = new MapPosition(bounding.getCenterPoint(), zoom);
//
//        getModel().mapViewPosition.setMapPosition(position);
//    }
//
//    @Override
//    public void zoomOut() {
//        model.mapViewPosition.zoomOut();
//    }
//
//    @Override
//    public void zoomIn() {
//        model.mapViewPosition.zoomIn();
//    }
//
//    @Override
//    public void requestRedraw() {
//        this.invalidate();
//    }
//
//    @Override
//    public void add(MapLayerInterface l) {
//        layers.add(l);
//    }
//
//    @Override
//    public MapContext getMContext() {
//        return mcontext;
//    }
//
//    @Override
//    public View toView() {
//        return this;
//    }
//
//    @Override
//    public void reDownloadTiles() {
//
//    }
//
//    @Override
//    public void onChange() {
//
//    }
//
//    @Override
//    public void addLayer(Layer layer) {
//
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    @Override
//    public void destroyAll() {
//
//    }
//
//    @Override
//    public BoundingBox getBoundingBox() {
//        return MapPositionUtil.getBoundingBox(this.model.mapViewPosition.getMapPosition(),
//                getDimension(), this.model.displayModel.getTileSize());
//    }
//
//    @Override
//    public Dimension getDimension() {
//        return new Dimension(getWidth(), getHeight());    }
//
//    @Override
//    public FpsCounter getFpsCounter() {
//        return null;
//    }
//
//    @Override
//    public FrameBuffer getFrameBuffer() {
//        return null;
//    }
//
//    @Override
//    public LayerManager getLayerManager() {
//        return null;
//    }
//
//    @Override
//    public MapScaleBar getMapScaleBar() {
//        return null;
//    }
//
//    @Override
//    public MapViewProjection getMapViewProjection() {
//        return projection;
//    }
//
//    @Override
//    public Model getModel() {
//        return model;
//    }
//
//    @Override
//    public void repaint() {
//        this.requestRedraw();
//    }
//
//    @Override
//    public void setCenter(LatLong center) {
//        model.mapViewPosition.setCenter(center);
//    }
//
//    @Override
//    public void setMapScaleBar(MapScaleBar mapScaleBar) {
//
//    }
//
//    @Override
//    public void setZoomLevel(byte zoomLevel) {
//        model.mapViewPosition.setZoomLevel(zoomLevel);
//    }
//
//    @Override
//    public void setZoomLevelMax(byte zoomLevelMax) {
//        model.mapViewPosition.setZoomLevelMax(zoomLevelMax);
//    }
//
//    @Override
//    public void setZoomLevelMin(byte zoomLevelMin) {
//        model.mapViewPosition.setZoomLevelMin(zoomLevelMin);
//    }
//}
//
