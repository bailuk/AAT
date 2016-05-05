package ch.bailu.aat.views.map.overlay.grid;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.constants.GeoConstants;

import android.graphics.Point;
import ch.bailu.aat.coordinates.WGS84Sexagesimal;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.services.dem.ElevationProvider;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;

public class WGS84Overlay extends OsmOverlay implements GeoConstants{
    
    
    private final ElevationProvider elevation;
    private final AltitudeDescription altitudeDescription;
    
    public WGS84Overlay(AbsOsmView osmPreview, ElevationProvider e) {
        super(osmPreview);
        elevation=e;
        altitudeDescription= new AltitudeDescription(getContext());
    }

    
    @Override
    public void draw(MapPainter painter) {
        drawGrid(painter);
        drawLabel(painter);
    }

    
    private void drawGrid(MapPainter painter) {
        final Point pixel = painter.projection.getCenterPixel();
        painter.canvas.drawVLine(pixel.x);
        painter.canvas.drawHLine(pixel.y);
        painter.canvas.drawPoint(pixel);
    }
    
    
    private void drawLabel(MapPainter painter) {
        final IGeoPoint point = painter.projection.getCenterPoint();
        
        final short ele = elevation.getElevation(point.getLatitudeE6(), point.getLongitudeE6());
        
        painter.canvas.drawTextBottom(altitudeDescription.getValueUnit(ele),3);
        
        painter.canvas.drawTextBottom(new WGS84Sexagesimal(point).toString(),2);
        painter.canvas.drawTextBottom(String.format("%.6f/%.6f", 
                ((double)point.getLatitudeE6()/1E6), 
                ((double)point.getLongitudeE6()/1E6)),
                1);
    }
}
