package ch.bailu.aat.map.osm.overlay.gpx.legend;

import android.graphics.Color;

import ch.bailu.aat.map.osm.AbsOsmView;
import ch.bailu.aat.map.osm.overlay.MapPainter;
import ch.bailu.aat.map.osm.overlay.MapTwoNodes;
import ch.bailu.aat.map.osm.overlay.gpx.GpxOverlay;

public class GpxLegendOverlay extends GpxOverlay {

    private final LegendWalker walker;
    
    public GpxLegendOverlay(AbsOsmView o, LegendWalker w) {
        super(o, Color.DKGRAY);
        walker=w;
    }

    @Override
    public void draw(MapPainter p) {
        MapViewContext context = new MapViewContext(p, new MapTwoNodes(p));
        
        walker.setContext(context);
        walker.walkTrack(getGpxList());
    }

}
