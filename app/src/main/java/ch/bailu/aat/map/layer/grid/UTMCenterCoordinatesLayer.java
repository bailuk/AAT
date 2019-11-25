package ch.bailu.aat.map.layer.grid;

import android.content.Context;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.coordinates.UTMCoordinates;

public final class UTMCenterCoordinatesLayer extends CenterCoordinatesLayer{
    public UTMCenterCoordinatesLayer(Context c) {
        super(c);
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new UTMCoordinates(p);
    }
}
