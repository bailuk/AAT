package ch.bailu.aat.map.layer.grid;

import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public abstract class CenterCoordinatesLayer implements MapLayerInterface {


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawOnTop(MapContext mc) {
        final LatLong point = mc.getMetrics().getBoundingBox().getCenterPoint();

        mc.draw().textBottom(getCoordinates(point).toString(), 1);
    }

    @Override
    public void drawInside(MapContext mc) {

    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }


    public abstract MeterCoordinates getCoordinates(LatLong p);


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
