package ch.bailu.aat_lib.map.layer.grid;


import org.mapsforge.core.model.LatLong;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.util.Point;

public final class ElevationLayer implements MapLayerInterface {

    private final static int MIN_ZOOM_LEVEL = 7;

    private final AltitudeDescription altitudeDescription;
    private final ServicesInterface services;

    public ElevationLayer(ServicesInterface services, StorageInterface storage) {
        altitudeDescription = new AltitudeDescription(storage);
        this.services = services;
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public void drawInside(MapContext mcontext) {
    }

    @Override
    public void drawForeground(MapContext mcontext) {
        byte zoomLevel = (byte) mcontext.getMetrics().getZoomLevel();
        final LatLong point = mcontext.getMapView().getMapViewPosition().getCenter();

        drawElevation(services, mcontext, zoomLevel, point);
    }


    private void drawElevation(ServicesInterface sc, MapContext mc, int zoom, final LatLong point) {
        if (zoom > MIN_ZOOM_LEVEL) {
            sc.insideContext(() -> {
                final short ele = sc.
                        getElevationService().
                        getElevation(point.getLatitudeE6(), point.getLongitudeE6());
                mc.draw().textBottom(altitudeDescription.getValueUnit(ele), 2);

            });
        }
    }

    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }

    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
