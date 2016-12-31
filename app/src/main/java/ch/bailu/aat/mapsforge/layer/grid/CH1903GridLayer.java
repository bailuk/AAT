package ch.bailu.aat.mapsforge.layer.grid;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.CH1903Coordinates;
import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.mapsforge.layer.context.MapContext;

public class CH1903GridLayer extends MeterGridLayer {
    public CH1903GridLayer(MapContext cl) {
        super(cl);
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new CH1903Coordinates(p);
    }


    @Override
    public void draw(BoundingBox b, byte z, Canvas c, org.mapsforge.core.model.Point p) {
        if (CH1903Coordinates.inSwitzerland(b.getCenterPoint()))
            super.draw(b,z,c,p);
    }








    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
