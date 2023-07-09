package ch.bailu.aat_lib.gpx.tools;

import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public class Attacher extends GpxListWalker {
    private final GpxList newList;

    private boolean newSegment = true;

    public Attacher(GpxList base) {
        newList = base;
    }


    @Override
    public boolean doList(GpxList toAttach) {
        newSegment = true;
        return true;
    }

    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        newSegment = true;
        return true;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return true;
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (newSegment) {
            newList.appendToNewSegment(point.getPoint(), point.getAttributes());

            newSegment = false;
        } else {
            newList.appendToCurrentSegment(point.getPoint(), point.getAttributes());
        }
    }


    public GpxList getNewList() {
        return newList;
    }

}
