package ch.bailu.aat_lib.map.layer.grid;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.MeterCoordinates;
import ch.bailu.aat_lib.coordinates.UTMCoordinates;
import ch.bailu.aat_lib.map.Point;
import ch.bailu.aat_lib.map.layer.grid.MeterGridLayer;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class UTMGridLayer extends MeterGridLayer {
    public UTMGridLayer(StorageInterface s) {
        super(s);
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new UTMCoordinates(p);
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
