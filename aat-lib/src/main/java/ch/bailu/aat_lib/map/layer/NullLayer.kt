package ch.bailu.aat_lib.map.layer;


import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class NullLayer implements MapLayerInterface {

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void drawInside(MapContext mcontext) {}

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key) {}
}
