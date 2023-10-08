package ch.bailu.aat_lib.map.layer.grid;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.CH1903Coordinates;
import ch.bailu.aat_lib.coordinates.MeterCoordinates;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.grid.CenterCoordinatesLayer;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class CH1903CenterCoordinatesLayer extends CenterCoordinatesLayer {
    public CH1903CenterCoordinatesLayer(ServicesInterface services, StorageInterface storage) {
        super(services, storage);
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
