package ch.bailu.aat.map.layer;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.Attachable;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;

public interface MapLayerInterface extends Attachable,
        OnPreferencesChanged {

    void onLayout(boolean changed, int l, int t, int r, int b);
    void drawInside(MapContext mcontext);
    void drawForeground(MapContext mcontext);

    boolean onTap(Point tapPos);
}
