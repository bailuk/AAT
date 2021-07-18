package ch.bailu.aat.map.layer.grid;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.coordinates.UTMCoordinates;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class UTMCenterCoordinatesLayer extends CenterCoordinatesLayer{
    public UTMCenterCoordinatesLayer(StorageInterface storageInterface) {
        super(storageInterface);
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new UTMCoordinates(p);
    }
}
