package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Point;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;

public class GpxAltitudeLengendOverlay extends GpxLegendOverlay {

    
    private final GpxListWalker markerWalker=new MarkerAltitudeWalker();
    private final DistanceDescription description;

    
    public GpxAltitudeLengendOverlay(AbsOsmView osmPreview, int id) {
        super(osmPreview, id);
        description = new DistanceDescription(osmPreview.getContext());
    }

    
    @Override
    public void draw(MapPainter painter) {
        super.draw(painter);
        
        int type = getGpxList().getDelta().getType();
        
        if (type == GpxBigDelta.TRK) 
            markerWalker.walkTrack(getGpxList());
    }

    
    @Override
    public void drawLegend(MapPainter painter, Point pixel, int value) {
        drawText(pixel, description.getAltitudeDescription(value));
    }


    @Override
    public void drawLegend(MapPainter painter, Point pixel, float value) {
        drawText(pixel, description.getAltitudeDescription(value));
    }
}
