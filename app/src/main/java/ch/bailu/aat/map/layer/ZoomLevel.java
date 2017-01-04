package ch.bailu.aat.map.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import java.util.Locale;

import ch.bailu.aat.map.MapContext;

public class ZoomLevel implements MapLayerInterface {

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void draw(MapContext mcontext) {
        mcontext.draw().textTop(String.format((Locale)null,"Zoomlevel*: %d", mcontext.getMetrics().getZoomLevel()), 2);
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
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
