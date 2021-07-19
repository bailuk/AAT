package ch.bailu.aat.map.layer.grid;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat_lib.coordinates.CH1903Coordinates;
import ch.bailu.aat_lib.coordinates.MeterCoordinates;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class CH1903GridLayer extends MeterGridLayer {
    public CH1903GridLayer(MapContext cl) {
        super(cl.getContext());
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new CH1903Coordinates(p);
    }


    @Override
    public void drawInside(MapContext c) {
        if (CH1903Coordinates.inSwitzerland(c.getMetrics().getBoundingBox().getCenterPoint()))
            super.drawInside(c);
    }



    @Override
    public void drawForeground(MapContext c) {
        if (CH1903Coordinates.inSwitzerland(c.getMetrics().getBoundingBox().getCenterPoint()))
            super.drawForeground(c);
    }







    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}


    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}
