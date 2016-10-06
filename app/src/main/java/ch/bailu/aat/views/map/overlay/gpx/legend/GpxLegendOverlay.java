package ch.bailu.aat.views.map.overlay.gpx.legend;

import android.graphics.Color;

import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.MapTwoNodes;
import ch.bailu.aat.views.map.overlay.gpx.GpxOverlay;

public class GpxLegendOverlay extends GpxOverlay {

    private final LegendWalker walker;
    
    public GpxLegendOverlay(AbsOsmView osmPreview, int id, LegendWalker w) {
        super(osmPreview, id, Color.DKGRAY);
        walker=w;
    }

    @Override
    public void draw(MapPainter p) {
        MapViewContext context = new MapViewContext(p, new MapTwoNodes(p));
        
        walker.setContext(context);
        walker.walkTrack(getGpxList());
    }

}
