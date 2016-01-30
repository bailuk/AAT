package ch.bailu.aat.views.map.overlay.grid;

import org.osmdroid.api.IGeoPoint;

import android.graphics.Point;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.services.srtm.ElevationProvider;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;

public class AltitudeOverlay extends OsmOverlay {
    private static final int MIN_ZOOM_LEVEL=10;
    
    private final ElevationProvider elevation;
    
    private final AltitudeDescription altitudeDescription;
    private final DistanceDescription distanceDescription;
    
    private final GridMetricScaler scaler = new GridMetricScaler();
    private final Point pixel=new Point();
    private float centerElevation=0f;

    
    public AltitudeOverlay(AbsOsmView osmView, ElevationProvider e) {
        super(osmView);
        elevation=e;
        altitudeDescription=new AltitudeDescription(getContext());
        distanceDescription=new DistanceDescription(getContext());
    }

    @Override
    public void draw(MapPainter painter) {
        if (getMapView().getZoomLevel() > MIN_ZOOM_LEVEL) {
            scaler.findOptimalScale(painter.projection.getShortDistance()/2);
        
            if (scaler.getOptimalScale()>0) {
                drawGrid(painter);
                painter.canvas.drawTextTop(distanceDescription.getDistanceDescriptionRounded(scaler.getOptimalScale()),1);
            }
        }
    }

    
    private void drawGrid(MapPainter painter) {
        Point center = painter.projection.getCenterPixel();
        int dist=painter.projection.getPixelFromDistance(scaler.getOptimalScale());

        final IGeoPoint point = painter.projection.fromPixels(
                center.x-painter.projection.screen.left, 
                center.y-painter.projection.screen.top);
        
        centerElevation=elevation.getElevation(point);
        
        painter.canvas.drawTextBottom(altitudeDescription.getValueUnit(centerElevation), 0);
        
        for(int x=-1; x<2; x++) {
            for(int y=-1; y<2; y++) {
                pixel.x=center.x+x*dist;
                pixel.y=center.y+y*dist;
                drawAltitudePoint(painter, pixel);
            }
            
        }
    }
    
    
    private void drawAltitudePoint(MapPainter painter, Point pixel) {
        final IGeoPoint point = painter.projection.fromPixels(
                pixel.x-painter.projection.screen.left, 
                pixel.y-painter.projection.screen.top);
        
        float alt=elevation.getElevation(point) - centerElevation;
        
        if (alt !=0f) 
            painter.canvas.drawText(altitudeDescription.getValue(alt), pixel);
        
        
        painter.canvas.drawPoint(pixel);
        
    }
}
