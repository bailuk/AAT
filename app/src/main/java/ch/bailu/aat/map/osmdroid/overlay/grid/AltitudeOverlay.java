package ch.bailu.aat.map.osmdroid.overlay.grid;

import android.graphics.Point;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.services.dem.ElevationProvider;
import ch.bailu.aat.map.osmdroid.AbsOsmView;
import ch.bailu.aat.map.osmdroid.overlay.MapPainter;
import ch.bailu.aat.map.osmdroid.overlay.OsmOverlay;

public class AltitudeOverlay extends OsmOverlay {
    private static final int MIN_ZOOM_LEVEL=10;
    private static final int POINTS_ON_CIRCLE=10;
    
    
    private final ElevationProvider elevation;
    
    private final AltitudeDescription altitudeDescription;
    private final DistanceDescription distanceDescription;
    
    private final GridMetricScaler scaler = new GridMetricScaler();
    private final Point pixel=new Point();
    private       Point center;
    
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
            
            center = painter.projection.getCenterPixel();
            
            
            scaler.findOptimalScale(painter.projection.getShortDistance()/2);
        
            if (scaler.getOptimalScale()>0) {
                centerElevation = getCenterElevation(painter);
                
                drawGrid(painter);
                painter.canvas.drawTextTop(distanceDescription.getDistanceDescriptionRounded(scaler.getOptimalScale()),1);
                
                centerElevation = getCenterElevation(painter);
                painter.canvas.drawTextBottom(altitudeDescription.getValueUnit(centerElevation), 0);
                
            }
        }
    }


    private float getCenterElevation(MapPainter painter) {
        final IGeoPoint point = painter.projection.fromPixels(
                center.x-painter.projection.screen.left, 
                center.y-painter.projection.screen.top);
        
        return elevation.getElevation(point.getLatitudeE6(), point.getLongitudeE6());
    }
    
    
    private void drawGrid(MapPainter painter) {
        int dist=painter.projection.getPixelFromDistance(scaler.getOptimalScale());
        
        double step=(2.d*Math.PI) / POINTS_ON_CIRCLE;
        
        for (int p=0; p< POINTS_ON_CIRCLE; p++) {
            drawAltitudePoint(painter, dist, p*step);
        }
        drawAltitudePoint(painter, center);
        
    }
    
    
    
    private void drawAltitudePoint(MapPainter painter, int r, double radians) {
        pixel.x = center.x + (int) (r * Math.sin(radians));
        pixel.y = center.y + (int) (r * Math.cos(radians));
        drawAltitudePoint(painter, pixel);
    }
    
    
    private void drawAltitudePoint(MapPainter painter, Point pixel) {
        final IGeoPoint point = painter.projection.fromPixels(
                pixel.x-painter.projection.screen.left, 
                pixel.y-painter.projection.screen.top);
        
        float alt=elevation.getElevation(point.getLatitudeE6(), point.getLongitudeE6()) - centerElevation;
        
        if (alt !=0f) 
            painter.canvas.drawText(altitudeDescription.getValue(alt), pixel);
        
        
        painter.canvas.drawPoint(pixel);
        
    }
}
