package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Point;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.SpeedDescription;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;


public class GpxSpeedLegendOverlay extends GpxLegendOverlay {

    private final GpxListWalker markerWalker=new MarkerSpeedWalker();
    private final SpeedDescription description;
    
    
    public GpxSpeedLegendOverlay(AbsOsmView osmPreview, int id) {
        super(osmPreview, id);
        description = new AverageSpeedDescription(osmPreview.getContext());
    }

    
    @Override
    public void draw(MapPainter painter) {
        super.draw(painter);
        
        int type = getGpxList().getDelta().getType();
        
        if (type == GpxBigDelta.TRK) 
            markerWalker.walkTrack(getGpxList());
    }
    
    
    @Override
    public void drawLegend(MapPainter painter, Point pixel, float value) {
        drawText(pixel, description.getSpeedDescription(value));
    }



    @Override
    public void drawLegend(MapPainter painter, Point pixel, int value) {
    }

}
