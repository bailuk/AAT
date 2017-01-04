package ch.bailu.aat.map;

import android.content.Context;

import ch.bailu.aat.services.ServiceContext;

public interface MapContext {
    MapMetrics getMetrics();
    MapDraw draw();
    ServiceContext getSContext();
    Context getContext();
    String getSolidKey();
    TwoNodes getTwoNodes();
    MapViewInterface getMapView();
}
