package ch.bailu.aat.map.layer.grid;

import android.content.Context;
import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public class ElevationLayer implements MapLayerInterface {

    private final static int MIN_ZOOM_LEVEL=7;

    private final AltitudeDescription altitudeDescription;

    public ElevationLayer(Context c) {
        altitudeDescription= new AltitudeDescription(c);
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


    private void drawElevation(MapContext mc, int zoom, LatLong point) {
        if (zoom > MIN_ZOOM_LEVEL && mc.getSContext().lock()) {
            final short ele = mc.getSContext().getElevationService().getElevation(point.getLatitudeE6(), point.getLongitudeE6());
            mc.draw().textBottom(altitudeDescription.getValueUnit(ele),2);
            mc.getSContext().free();
        }
    }
    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
