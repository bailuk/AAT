package ch.bailu.aat.map.layer.grid;

import android.content.Context;
import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;

import java.util.Locale;

import ch.bailu.aat.coordinates.WGS84Sexagesimal;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public class WGS84Layer implements MapLayerInterface {

    private final ElevationLayer elevation;
    private final Crosshair crosshair;


    public WGS84Layer (Context c) {
        elevation = new ElevationLayer(c);
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
    public boolean onTap( org.mapsforge.core.model.Point tapXY) {
        return false;
    }





    private void drawCoordinates(MapContext clayer,LatLong point) {
        clayer.draw().textBottom(new WGS84Sexagesimal(point.getLatitude(), point.getLongitude()).toString(),1);
        clayer.draw().textBottom(
                String.format((Locale)null,"%.6f/%.6f",
                        (point.getLatitude()),
                        (point.getLongitude())),
                0);
    }





    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
