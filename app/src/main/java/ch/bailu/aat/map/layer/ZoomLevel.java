package ch.bailu.aat.map.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.ToDo;

public class ZoomLevel implements MapLayerInterface {

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void drawForeground(MapContext mcontext) {
        int zoomLevel = mcontext.getMetrics().getZoomLevel();
        String text = ToDo.translate("Zoomlevel: " + zoomLevel);

        mcontext.draw().textTop(text, 2);
    }

    @Override
    public void drawInside(MapContext mcontext) {}

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
