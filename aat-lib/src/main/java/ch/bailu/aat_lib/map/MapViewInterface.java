package ch.bailu.aat_lib.map;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.model.IMapViewPosition;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.LifeCycleInterface;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;

public interface MapViewInterface extends LifeCycleInterface {
    void frameBounding(BoundingBoxE6 boundingBox);

    void zoomOut();

    void zoomIn();

    void requestRedraw();


    void add(MapLayerInterface l);

    MapContext getMContext();

    void setZoomLevel(byte z);

    void setCenter(LatLong gpsLocation);

    void reDownloadTiles();

    IMapViewPosition getMapViewPosition();
}
