package ch.bailu.aat.map.layer.grid;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.openlocationcode.OpenLocationCode;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.description.FF;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public final class PlusCodesCenterCoordinatesLayer  implements MapLayerInterface {

    private final ElevationLayer elevation;
    private final Crosshair crosshair;

    public PlusCodesCenterCoordinatesLayer (Context c) {
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
        final OpenLocationCode center =
                new OpenLocationCode(point.latitude, point.longitude);

        final String code = center.getCode();
        clayer.draw().textBottom(code,1);

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
