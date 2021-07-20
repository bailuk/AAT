package ch.bailu.aat_lib.map.layer.grid;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.MeterCoordinates;
import ch.bailu.aat_lib.coordinates.UTMCoordinates;
import ch.bailu.aat_lib.map.layer.grid.CenterCoordinatesLayer;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class UTMCenterCoordinatesLayer extends CenterCoordinatesLayer {
    public UTMCenterCoordinatesLayer(ServicesInterface services, StorageInterface storage) {
        super(services, storage);
    }

    @Override
    public MeterCoordinates getCoordinates(LatLong p) {
        return new UTMCoordinates(p);
    }
}
