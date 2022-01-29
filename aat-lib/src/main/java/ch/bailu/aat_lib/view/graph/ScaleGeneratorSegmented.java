package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public class ScaleGeneratorSegmented extends ScaleGenerator {
    private int index = 0;
    private final Segment segment;

    public ScaleGeneratorSegmented(GraphPlotter p, Segment segment) {
        super(p);
        this.segment = segment;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return doDelta(marker.getSegmentSize());
    }

    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return doDelta(segment.getSegmentSize());
    }

    private boolean doDelta(int size) {

        if (segment.isAfter(index)) {
            return false;

        } else {
            int nextIndex = index + size;

            if (segment.isBefore(nextIndex)) {
                index = nextIndex;
                return false;
            }
        }
        return true;
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (segment.isInside(index)) {
            super.doPoint(point);
        }
        index++;
    }
}
