package ch.bailu.aat.map.mapsforge.layer.grid;

import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.coordinates.UTMCoordinates;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;

public class UTMGridLayer extends MeterGridLayer{
    public UTMGridLayer(MapContext cl) {
        super(cl);
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new UTMCoordinates(p);
    }



    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
