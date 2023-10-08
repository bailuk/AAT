package ch.bailu.aat_lib.map.layer.grid;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.Coordinates;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public abstract class CenterCoordinatesLayer implements MapLayerInterface {

    private final ElevationLayer elevation;

    public CenterCoordinatesLayer(ServicesInterface services, StorageInterface storage) {
        elevation = new ElevationLayer(services, storage);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawForeground(MapContext mc) {
        final LatLong point = mc.getMapView().getMapViewPosition().getCenter();

        mc.draw().textBottom(getCoordinates(point).toString(), 1);

        elevation.drawForeground(mc);
    }

    @Override
    public void drawInside(MapContext mc) {}

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }


    public abstract Coordinates getCoordinates(LatLong p);


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
