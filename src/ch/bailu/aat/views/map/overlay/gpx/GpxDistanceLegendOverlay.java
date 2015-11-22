package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Point;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;

public class GpxDistanceLegendOverlay extends GpxLegendOverlay {

    private final DistanceDescription description;
    

    private final GpxListWalker markerWalker=new MarkerDistanceWalker();
    private final GpxListWalker pointWalker=new PointDistanceWalker();
    
    public GpxDistanceLegendOverlay(AbsOsmView osmPreview, int id, boolean reset) {
        super(osmPreview, id, reset);
        description = new DistanceDescription(osmPreview.getContext());
    }

    
    @Override
    public void draw(MapPainter painter) {
        super.draw(painter);
        
        int type = getGpxList().getDelta().getType();
        
        if (type == GpxBigDelta.TRK) {
            markerWalker.walkTrack(getGpxList());
        } else if (type == GpxBigDelta.RTE) {
            pointWalker.walkTrack(getGpxList());
        }
    }
    
    @Override
    public void drawLegend(MapPainter painter, Point pixel, float value) {
        drawText(pixel, description.getDistanceDescription(value));
    }

    


    @Override
    public void drawLegend(MapPainter painter, Point pixel, int value) {
    }

}
