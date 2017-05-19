package ch.bailu.aat.map.layer.gpx.legend;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.map.MapContext;

public abstract class LegendWalker extends GpxListWalker {

    public LegendContext c;

    public void init(MapContext mc) {
        c=new LegendContext(mc);
    }


    @Override
    public boolean doList(GpxList track) {
        return
                track.getPointList().size() > 0 &&
                        c.isVisible(track.getDelta().getBoundingBox());
    }



    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return true;
    }
}
