package ch.bailu.aat.map;

import android.view.View;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.model.IMapViewPosition;

import ch.bailu.aat.dispatcher.LifeCycleInterface;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;

public interface MapViewInterface extends LifeCycleInterface {
    void frameBounding(BoundingBoxE6 boundingBox);

    void zoomOut();

    void zoomIn();

    void requestRedraw();


    void add(MapLayerInterface l);

    MapContext getMContext();

    void setZoomLevel(byte z);

    void setCenter(LatLong gpsLocation);

    void addView(View view);

    View toView();

    void reDownloadTiles();

    IMapViewPosition getMapViewPosition();
}
