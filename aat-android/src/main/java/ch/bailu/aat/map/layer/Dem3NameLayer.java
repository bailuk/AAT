package ch.bailu.aat.map.layer;

import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class Dem3NameLayer implements MapLayerInterface {


    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }


    @Override
    public void drawInside(MapContext mcontext) {
        final Dem3Coordinates c = new Dem3Coordinates(mcontext.getMapView().getMapViewPosition().getCenter());
        mcontext.draw().textBottom(c.toString(),3);
    }

     @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
