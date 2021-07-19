package ch.bailu.aat.map.layer.grid;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.To;
import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class ElevationLayer implements MapLayerInterface {

    private final static int MIN_ZOOM_LEVEL=7;

    private final AltitudeDescription altitudeDescription;

    public ElevationLayer(StorageInterface storageInterface) {
        altitudeDescription= new AltitudeDescription(storageInterface);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawInside(MapContext mcontext) {

    }

    @Override
    public void drawForeground(MapContext mcontext) {
        byte zoomLevel = (byte)mcontext.getMetrics().getZoomLevel();
        final LatLong point = mcontext.getMapView().getMapViewPosition().getCenter();


        drawElevation(mcontext, zoomLevel, point);
    }


    private void drawElevation(final MapContext mc, int zoom, final LatLong point) {
        if (zoom > MIN_ZOOM_LEVEL) {
            new InsideContext(To.scontext(mc)) {
                @Override
                public void run() {
                    final short ele = To.scontext(mc).
                            getElevationService().
                            getElevation(point.getLatitudeE6(), point.getLongitudeE6());
                    mc.draw().textBottom(altitudeDescription.getValueUnit(ele),2);

                }
            };

        }
    }
    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
