package ch.bailu.aat.map.layer.gpx;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.map.MapContext;

public class GpxVisibleLimit extends GpxListWalker {

    private int index = 0;

    private int firstPoint = -1;
    private int lastPoint = -1;

    private final MapContext mcontext;

    public GpxVisibleLimit(MapContext mc) {
        mcontext = mc;
    }

    @Override
    public boolean doList(GpxList track) {
        return mcontext.getMetrics().isVisible(track.getDelta().getBoundingBox());
    }


    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        boolean visible = mcontext.getMetrics().isVisible(segment.getBoundingBox());

        if (!visible) {
            index += segment.getSegmentSize();
        }
        return visible;
    }


    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        boolean visible =  mcontext.getMetrics().isVisible(marker.getBoundingBox());

        if (!visible) {
            index += marker.getSegmentSize();
        }
        return visible;
    }


    @Override
    public void doPoint(GpxPointNode point) {
        boolean visible = mcontext.getMetrics().isVisible(point);

        if (visible) {
            if (firstPoint < 0) {
                firstPoint = index;
            }
            lastPoint = index;
        }
        index++;
    }

    public int getFirstPoint() {
        return firstPoint;
    }

    public int getLastPoint() {
        return lastPoint;
    }
}
