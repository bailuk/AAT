package ch.bailu.aat.map.mapsforge.layer.gpx.legend;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxSegmentNode;

public abstract class LegendWalker extends GpxListWalker {

    public LegendContext c;

    public void init(LegendContext lc) {
        c=lc;
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
