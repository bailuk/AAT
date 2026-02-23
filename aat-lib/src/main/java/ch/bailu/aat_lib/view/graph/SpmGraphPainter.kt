package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public class SpmGraphPainter extends GpxListWalker {
    private final SpmEntry[] entries;

    private float distance=0;
    private final float minDistance;


    public SpmGraphPainter(SpmEntry[] entries, int minDistance) {
        this.entries = entries;
        this.minDistance = minDistance;

    }


    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return true;
    }


    @Override
    public void doPoint(GpxPointNode point) {
        for (SpmEntry e : entries) {
            distance += point.getDistance();

            e.incrementSummaryDistance(point.getDistance());
            e.plotIfDistance(point, minDistance, distance);
        }
    }

    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return true;
    }

    @Override
    public boolean doList(GpxList track) {
        return true;
    }
}