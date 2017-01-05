package ch.bailu.aat.map;

import android.view.View;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.map.layer.MapLayerInterface;

public interface MapViewInterface {
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
}
