package ch.bailu.aat.map.osmdroid.overlay;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.map.osmdroid.AbsOsmView;

public class Dem3NameOverlay extends OsmOverlay {
    
    public Dem3NameOverlay(AbsOsmView osmPreview) {
        super(osmPreview);
    }

    
    @Override
    public void draw(MapPainter painter) {
        final IGeoPoint point = painter.projection.getCenterPoint();
        
        final SrtmCoordinates c = new SrtmCoordinates(point);
        painter.canvas.drawTextBottom(c.toString(),4);
       
    }
}
