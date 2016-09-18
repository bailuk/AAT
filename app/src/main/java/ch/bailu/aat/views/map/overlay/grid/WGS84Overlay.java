package ch.bailu.aat.views.map.overlay.grid;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.constants.GeoConstants;

import android.graphics.Point;

import java.util.Locale;

import ch.bailu.aat.coordinates.WGS84Sexagesimal;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;

public class WGS84Overlay extends OsmOverlay implements GeoConstants{
    private final static int MIN_ZOOM_LEVEL=7;
    
    private final ServiceContext scontext;
    private final AltitudeDescription altitudeDescription;
    
    public WGS84Overlay(AbsOsmView osmPreview, ServiceContext sc) {
        super(osmPreview);
        scontext=sc;
        altitudeDescription= new AltitudeDescription(getContext());
    }

    
    @Override
    public void draw(MapPainter painter) {
        final IGeoPoint point = painter.projection.getCenterPoint();
        
        drawGrid(painter);
        drawCoordinates(painter, point);
        drawElevation(painter, point);
    }

    
    private void drawGrid(MapPainter painter) {
        final Point pixel = painter.projection.getCenterPixel();
        painter.canvas.drawVLine(pixel.x);
        painter.canvas.drawHLine(pixel.y);
        painter.canvas.drawPoint(pixel);
    }
    
    
    private void drawCoordinates(MapPainter painter, IGeoPoint point) {
        painter.canvas.drawTextBottom(new WGS84Sexagesimal(point).toString(),2);
        painter.canvas.drawTextBottom(
                String.format((Locale)null,"%.6f/%.6f",
                ((double)point.getLatitudeE6()/1E6), 
                ((double)point.getLongitudeE6()/1E6)),
                1);
    }
    
    private void drawElevation(MapPainter painter, IGeoPoint point) {
        if (getMapView().getZoomLevel() > MIN_ZOOM_LEVEL) {
            final short ele = scontext.getElevationService().getElevation(point.getLatitudeE6(), point.getLongitudeE6());
            painter.canvas.drawTextBottom(altitudeDescription.getValueUnit(ele),3);
        }
    }
}
