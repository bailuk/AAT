package ch.bailu.aat.map.layer.grid;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.coordinates.UTMCoordinates;

public class UTMCenterCoordinatesLayer extends CenterCoordinatesLayer{
    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new UTMCoordinates(p);
    }
}
