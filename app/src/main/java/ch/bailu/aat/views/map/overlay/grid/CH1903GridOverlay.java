package ch.bailu.aat.views.map.overlay.grid;

import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.coordinates.CH1903Coordinates;
import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;


public class CH1903GridOverlay extends MeterGridOverlay {
    public CH1903GridOverlay(AbsOsmView osmPreview) {
        super(osmPreview);
    }

    
    @Override
    public void draw(MapPainter painter) {
        if (CH1903Coordinates.inSwitzerland(painter.projection.getCenterPoint())) 
            super.draw(painter);
    }


    @Override
    public MeterCoordinates getCoordinates(GeoPoint p) {
        return new CH1903Coordinates(p);
    }
}
