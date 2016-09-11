package ch.bailu.aat.views.map.overlay.grid;

import org.osmdroid.util.GeoPoint;

import android.graphics.Point;
import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;

public abstract class MeterGridOverlay extends OsmOverlay {
    private static final int MIN_ZOOM_LEVEL=5;
    
    private final GridMetricScaler grid = new GridMetricScaler();
    private  Point centerPixel;
    
    private final DistanceDescription distanceDescription;
    
    
    public MeterGridOverlay(AbsOsmView osmPreview) {
        super(osmPreview);
        distanceDescription=new DistanceDescription(osmPreview.getContext());
    }
    
    @Override
    public void draw(MapPainter painter) {
        if (getMapView().getZoomLevel() > MIN_ZOOM_LEVEL) {
            
            grid.findOptimalScale(painter.projection.getShortDistance()/2);
            if (grid.getOptimalScale() > 0) {
                
                MeterCoordinates coordinates = getRoundedCoordinates(painter, grid.getOptimalScale());
                centerPixel = getCenterPixel(painter, coordinates);
                
                painter.canvas.drawGrid(centerPixel, 
                        painter.projection.getPixelFromDistance(grid.getOptimalScale()));
                
                painter.canvas.drawPoint(centerPixel);
                painter.canvas.drawTextTop(distanceDescription.getDistanceDescriptionRounded(grid.getOptimalScale()),1);
                painter.canvas.drawTextBottom(coordinates.toString(),1);
            }
        }
        
       
    }


    private Point getCenterPixel(MapPainter painter, MeterCoordinates c) {
        return painter.projection.toMapPixels(c.toGeoPoint());
    }
    
    private MeterCoordinates getRoundedCoordinates(MapPainter painter, int space) {
        GeoPoint center=painter.projection.getCenterPoint();
        MeterCoordinates c=getCoordinates(center);
        c.round(grid.getOptimalScale());
        return c;
    }
    
    public abstract MeterCoordinates getCoordinates(GeoPoint p);
    
}
