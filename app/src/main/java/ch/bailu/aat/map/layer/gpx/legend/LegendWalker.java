package ch.bailu.aat.map.layer.gpx.legend;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.map.MapContext;

public abstract class LegendWalker extends GpxListWalker {
    public static final int COLOR = 0x99ffffff;
    public static final int MIN_PIXEL_DISTANCE=100;


    public LegendContext c;

    public void init(MapContext lc) {
        c=new LegendContext(lc);
    }


    @Override
    public boolean doList(GpxList track) {
        if (track.getPointList().size() > 0 && c.isVisible(track.getDelta().getBoundingBox())) {
            return true;
        }
        return false;
    }



    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return true;
    }
}
