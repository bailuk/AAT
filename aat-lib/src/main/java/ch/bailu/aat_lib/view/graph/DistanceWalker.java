package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public class DistanceWalker extends GpxListWalker {
    private int index = 0;
    private float dstDelta = 0f;
    private float dstOffset = 0f;
    private final Segment segment;

    public DistanceWalker(Segment segment) {
        this.segment = segment;
    }

    @Override
    public boolean doList(GpxList track) {
        if (segment.isValid()) {
            return true;
        } else {
            dstDelta = track.getDelta().getDistance();
            return false;
        }
    }

    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return doDelta(segment.getSegmentSize(), segment.getDistance());
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return doDelta(marker.getSegmentSize(), marker.getDistance());
    }


    private boolean doDelta(int size, float distance) {

        if (segment.isAfter(index)) {
            return false;

        } else {
            int nextIndex = index + size;

            if (segment.isBefore(nextIndex)) {
                index = nextIndex;
                dstOffset += distance;
                return false;
            }
        }
        return true;
    }


    @Override
    public void doPoint(GpxPointNode point) {
        if (segment.isBefore(index)) {
            dstOffset += point.getDistance();

        } else if (!segment.isAfter(index)) {
            dstDelta += point.getDistance();
        }
        index++;
    }


    public float getDistanceDelta() {
        return dstDelta;
    }

    public float getDistanceOffset() {
        return dstOffset;
    }
}
