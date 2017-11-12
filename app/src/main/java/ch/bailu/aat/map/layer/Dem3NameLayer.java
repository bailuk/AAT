package ch.bailu.aat.map.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.map.MapContext;

public class Dem3NameLayer implements MapLayerInterface {


    @Override
    public void drawForeground(MapContext mcontext) {}


    @Override
    public void drawInside(MapContext mcontext) {

        final SrtmCoordinates c = new SrtmCoordinates(mcontext.getMapView().getMapViewPosition().getCenter());
        mcontext.draw().textBottom(c.toString(),3);
    }

    @Override
    public boolean onTap(Point tapXY) {
        return false;
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
