package ch.bailu.aat.map.layer.grid;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.CH1903Coordinates;
import ch.bailu.aat.coordinates.MeterCoordinates;

public class CH1903CenterCoordinatesLayer extends CenterCoordinatesLayer {
    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new CH1903Coordinates(p);
    }



}
