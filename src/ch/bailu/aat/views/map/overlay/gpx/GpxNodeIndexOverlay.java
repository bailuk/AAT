package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Point;
import ch.bailu.aat.gpx.GpxBigDelta;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;

public class GpxNodeIndexOverlay extends GpxLegendOverlay {
    private final GpxListWalker indexWalker=new PointIndexWalker();

    
    public GpxNodeIndexOverlay(OsmInteractiveView map, int id) {
        super(map, id);
    }

    
    @Override
    public void draw(MapPainter painter) {
        super.draw(painter);
        
        int type = getGpxList().getDelta().getType();
        
        if (type == GpxBigDelta.WAY) 
            indexWalker.walkTrack(getGpxList());
    }


    @Override
    public void drawLegend(MapPainter painter, Point pixel, float value) {}




    @Override
    public void drawLegend(MapPainter painter, Point pixel, int value) {
        drawText(pixel, String.valueOf(value));
    }

}
