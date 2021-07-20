package ch.bailu.aat_lib.gpx.tools;

import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxDeltaHelper;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.GpxPointFirstNode;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;

public class SimplifierDistance extends GpxListWalker {

    private final static float MIN_DISTANCE=25f;

    private GpxList newList;
    private boolean newSegment = true;

    private GpxPointNode lastPoint=null;


    @Override
    public boolean doList(GpxList track) {
        newList = new GpxList(track.getDelta().getType(), GpxListAttributes.NULL);

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
            newSegment = false;

            newList.appendToNewSegment(new GpxPoint(point), point.getAttributes());
            lastPoint = point;

        } else {

            if (isLastInSegment(point) || hasDistance(point)) {
                newList.appendToCurrentSegment(new GpxPoint(point), point.getAttributes());
                lastPoint = point;
            }

        }
    }


    private boolean hasDistance(GpxPointNode point) {
        return GpxDeltaHelper.getDistance(lastPoint, point) >= MIN_DISTANCE;
    }


    private boolean isLastInSegment(GpxPointNode point) {
        return point.getNext() == null || point.getNext() instanceof GpxPointFirstNode;
    }


    public GpxList getNewList() {
        return newList;
    }
}
