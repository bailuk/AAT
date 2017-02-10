package ch.bailu.aat.map.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.Attachable;

public interface MapLayerInterface extends Attachable,
        SharedPreferences.OnSharedPreferenceChangeListener{

    void onLayout(boolean changed, int l, int t, int r, int b);
    void drawInside(MapContext mcontext);
    void drawForeground(MapContext mcontext);

    boolean onTap(Point tapPos);
}
