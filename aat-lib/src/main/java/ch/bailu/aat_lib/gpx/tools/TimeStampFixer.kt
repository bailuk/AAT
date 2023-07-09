package ch.bailu.aat_lib.gpx.tools;

import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.GpxPointFirstNode;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;

public class TimeStampFixer extends GpxListWalker {


    private GpxList newList;
    private boolean newSegment = true;


    private long previousTimeStamp =0;


    @Override
    public boolean doList(GpxList track) {
        newList = new GpxList(track.getDelta().getType(), GpxListAttributes.NULL);
        return true;
    }


    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        previousTimeStamp = 0;
        newSegment = true;
        return true;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return true;
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (hasNoErrors(point)) {
            if (newSegment) {
                newSegment = false;
                newList.appendToNewSegment(new GpxPoint(point), point.getAttributes());

            } else {
                newList.appendToCurrentSegment(new GpxPoint(point), point.getAttributes());
            }

            previousTimeStamp = point.getTimeStamp();
        }
    }


    private final static long FIVE_MINUTES = 1000 * 60 * 5;

    private boolean hasNoErrors(GpxPointNode point) {

        if (timeMoreThanPrevious(point)) {
            if (isLastInSegment(point)) {
                return timeNoSkip(point, FIVE_MINUTES);
            } else {
                return timeLessThanNext(point, (GpxPointNode) point.getNext());
            }

        }

        return false;
    }




    private boolean timeMoreThanPrevious(GpxPointNode point) {
        return previousTimeStamp < point.getTimeStamp();
    }


    private boolean timeLessThanNext(GpxPointNode point, GpxPointNode next) {
         return point.getTimeStamp() < next.getTimeStamp();
    }


    private boolean timeNoSkip(GpxPointNode point, long time) {
        return (previousTimeStamp + time) > point.getTimeStamp();
    }

    private boolean isLastInSegment(GpxPointNode point) {
        return point.getNext() == null || point.getNext() instanceof GpxPointFirstNode;
    }

    public GpxList getNewList() {
        return newList;
    }
}
