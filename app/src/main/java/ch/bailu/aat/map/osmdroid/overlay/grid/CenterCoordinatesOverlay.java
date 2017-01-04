package ch.bailu.aat.map.osmdroid.overlay.grid;

import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.map.osmdroid.AbsOsmView;
import ch.bailu.aat.map.osmdroid.overlay.MapPainter;
import ch.bailu.aat.map.osmdroid.overlay.OsmOverlay;

public abstract class CenterCoordinatesOverlay extends OsmOverlay {

    public CenterCoordinatesOverlay(AbsOsmView osmPreview) {
        super(osmPreview);
    }



    @Override
    public void draw(MapPainter painter) {
        final GeoPoint point = painter.projection.getCenterPoint();
        painter.canvas.drawTextBottom(getCoordinates(point).toString(),2); 
    }

    
    public abstract MeterCoordinates getCoordinates(GeoPoint p);

}
