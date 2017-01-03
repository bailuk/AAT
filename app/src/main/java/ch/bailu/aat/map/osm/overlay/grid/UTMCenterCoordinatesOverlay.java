package ch.bailu.aat.map.osm.overlay.grid;

import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.coordinates.UTMCoordinates;
import ch.bailu.aat.map.osm.AbsOsmView;

public class UTMCenterCoordinatesOverlay extends CenterCoordinatesOverlay {

    public UTMCenterCoordinatesOverlay(AbsOsmView osmPreview) {
        super(osmPreview);
    }


    @Override
    public MeterCoordinates getCoordinates(GeoPoint p) {
        return new UTMCoordinates(p);
    }

}
