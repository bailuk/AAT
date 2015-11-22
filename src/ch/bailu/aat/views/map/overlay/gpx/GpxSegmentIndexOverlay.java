package ch.bailu.aat.views.map.overlay.gpx;

import android.graphics.Point;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;

public class GpxSegmentIndexOverlay extends GpxLegendOverlay {
    private final GpxListWalker indexWalker=new SegmentIndexWalker();

    
    public GpxSegmentIndexOverlay(AbsOsmView osmPreview, int id) {
        super(osmPreview, id);
    }

    
    @Override
    public void draw(MapPainter painter) {
        super.draw(painter);
        indexWalker.walkTrack(getGpxList());
    }
    
    @Override
    public void drawLegend(MapPainter painter, Point pixel, int value) {
        drawText(pixel, String.valueOf(value));

    }

    @Override
    public void drawLegend(MapPainter painter, Point pixel, float value) {
    }


}
