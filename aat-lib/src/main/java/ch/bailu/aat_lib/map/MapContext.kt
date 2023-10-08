package ch.bailu.aat_lib.map;


public interface MapContext {
    MapMetrics getMetrics();
    MapDraw draw();
    String getSolidKey();
    TwoNodes getTwoNodes();
    MapViewInterface getMapView();
}
