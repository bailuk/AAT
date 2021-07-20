package ch.bailu.aat_lib.map.layer.grid;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.CH1903Coordinates;
import ch.bailu.aat_lib.coordinates.MeterCoordinates;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class CH1903GridLayer extends MeterGridLayer {
    public CH1903GridLayer(StorageInterface s) {
        super(s);
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new CH1903Coordinates(p);
    }

    @Override
    public void drawInside(MapContext mc) {
        if (CH1903Coordinates.inSwitzerland(mc.getMetrics().getBoundingBox().getCenterPoint()))
            super.drawInside(mc);
    }

    @Override
    public void drawForeground(MapContext mc) {
        if (CH1903Coordinates.inSwitzerland(mc.getMetrics().getBoundingBox().getCenterPoint()))
            super.drawForeground(mc);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
