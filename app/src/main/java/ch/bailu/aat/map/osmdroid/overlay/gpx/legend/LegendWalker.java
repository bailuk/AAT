package ch.bailu.aat.map.osmdroid.overlay.gpx.legend;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxSegmentNode;

public abstract class LegendWalker extends GpxListWalker {
 

    public MapViewContext c;
    
    public void setContext(MapViewContext context) {
        c=context;
    }
    
    
    @Override
    public boolean doList(GpxList track) {
        if (track.getPointList().size() > 0 && c.isVisible(track.getDelta().getBoundingBox())) {
            c.createDrawable();
            return true;
        }
        return false;
    }


   
    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return true;
    }
}
    


