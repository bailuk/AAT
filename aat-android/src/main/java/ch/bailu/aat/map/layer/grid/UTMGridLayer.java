package ch.bailu.aat.map.layer.grid;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.To;
import ch.bailu.aat_lib.coordinates.MeterCoordinates;
import ch.bailu.aat_lib.coordinates.UTMCoordinates;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class UTMGridLayer extends MeterGridLayer{
    public UTMGridLayer(MapContext mc) {
        super(To.context(mc));
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new UTMCoordinates(p);
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
    public void onAttached() {}

    @Override
    public void onDetached() {}
}
