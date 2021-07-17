package ch.bailu.aat.gpx.tools;

import android.location.Location;

import ch.bailu.aat.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.GpxPointFirstNode;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;

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
                lastBearing = getBearing(lastPoint, (GpxPointInterface) lastPoint.getNext());
            }

        } else {

            if (isLastInSegment(point) || hasBearingChanged(point)) {

                newList.appendToCurrentSegment(new GpxPoint(point), point.getAttributes());
                lastPoint = point;
                lastBearing = currentBearing;

            }

        }
    }

    public static double getBearing(GpxPointInterface a, GpxPointInterface b) {
        Location la = new Location("");
        la.setLatitude(a.getLatitude());
        la.setLongitude(a.getLongitude());

        Location lb = new Location("");
        lb.setLatitude(b.getLatitude());
        lb.setLongitude(b.getLongitude());

        return la.bearingTo(lb);
    }

    private boolean hasBearingChanged(GpxPointNode point) {
        currentBearing = getBearing(point, (GpxPointInterface) point.getNext());

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
