package ch.bailu.aat.gpx.tools;

import ch.bailu.aat.gpx.GpxListAttributes;
import ch.bailu.aat.gpx.GpxDeltaHelper;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointFirstNode;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;

public class SimplifierBearing extends GpxListWalker {

    private final static double MIN_BEARING_DELTA = 10d;


    private GpxList newList;
    private boolean newSegment = true;

    private double lastBearing;
    private double currentBearing;


    @Override
    public boolean doList(GpxList track) {
        newList = new GpxList(track.getDelta().getType(),
                GpxListAttributes.NULL);
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
        GpxPointNode lastPoint = null;
        if (newSegment) {
            newSegment = false;

            newList.appendToNewSegment(new GpxPoint(point), point.getAttributes());
            lastPoint = point;


            if (!isLastInSegment(point)) {
                lastBearing = GpxDeltaHelper.getBearing(lastPoint, (GpxPointInterface) lastPoint.getNext());
            }

        } else {

            if (isLastInSegment(point) || hasBearingChanged(point)) {

                newList.appendToCurrentSegment(new GpxPoint(point), point.getAttributes());
                lastPoint = point;
                lastBearing = currentBearing;

            }

        }
    }

    private boolean hasBearingChanged(GpxPointNode point) {
        currentBearing = GpxDeltaHelper.getBearing(point, (GpxPointInterface) point.getNext());

        double delta = Math.abs(currentBearing - lastBearing);

        return delta >= MIN_BEARING_DELTA;
    }


    private boolean isLastInSegment(GpxPointNode point) {
        return point.getNext() == null || point.getNext() instanceof GpxPointFirstNode;
    }


    public GpxList getNewList() {
        return newList;
    }
}
