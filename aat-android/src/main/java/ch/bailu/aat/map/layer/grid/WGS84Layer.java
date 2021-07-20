package ch.bailu.aat.map.layer.grid;


import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.WGS84Coordinates;
import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class WGS84Layer implements MapLayerInterface {

    private final ElevationLayer elevation;
    private final Crosshair crosshair;


    public WGS84Layer(StorageInterface storageInterface) {
        elevation = new ElevationLayer(storageInterface);
        crosshair = new Crosshair();
    }
    @Override
    public void drawForeground(MapContext mcontext) {
        final LatLong point = mcontext.getMapView().getMapViewPosition().getCenter();

        crosshair.drawForeground(mcontext);
        drawCoordinates(mcontext, point);
        elevation.drawForeground(mcontext);
    }


    @Override
    public void drawInside(MapContext mcontext) {


    }

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }


    private void drawCoordinates(MapContext clayer,LatLong point) {
        final FF f = FF.f();

        clayer.draw().textBottom(new WGS84Coordinates(point).toString(),1);
        clayer.draw().textBottom(f.N6.format(point.latitude) + "/" + f.N6.format(point.getLongitude()),0);
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
