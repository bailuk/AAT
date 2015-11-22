package ch.bailu.aat.views.map.overlay.grid;

import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.coordinates.CH1903Coordinates;
import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.views.map.AbsOsmView;

public class CH1903CenterCoordinatesOverlay extends CenterCoordinatesOverlay {

    public CH1903CenterCoordinatesOverlay(AbsOsmView osmPreview) {
        super(osmPreview);
    }

    @Override
    public MeterCoordinates getCoordinates(GeoPoint p) {
        return new CH1903Coordinates(p);
    }



}
