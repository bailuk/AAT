package ch.bailu.aat.map.layer.grid;

import android.content.Context;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.CH1903Coordinates;
import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.map.MapContext;

public final class CH1903CenterCoordinatesLayer extends CenterCoordinatesLayer {
    public CH1903CenterCoordinatesLayer(Context c) {
        super(c);
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new CH1903Coordinates(p);
    }


    @Override
    public void drawForeground(MapContext c) {
        if (CH1903Coordinates.inSwitzerland(c.getMetrics().getBoundingBox().getCenterPoint()))
            super.drawForeground(c);
    }
}
