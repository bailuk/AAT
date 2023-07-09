package ch.bailu.aat_lib.gpx.tools;

import ch.bailu.aat_lib.coordinates.LatLongInterface;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
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

    /**
     * Initial bearing (azimuth) from point a to point b
     * https://www.igismap.com/formula-to-find-bearing-or-heading-angle-between-two-points-latitude-longitude/
     * http://www.movable-type.co.uk/scripts/latlong.html
     *
     * @param p1 from this point
     * @param p2 to this point
     * @return bearing in degrees east of true north
     */
    public static float getBearing(LatLongInterface p1, LatLongInterface p2) {
        final double la1 = Math.toRadians(p1.getLatitude());
        final double la2 = Math.toRadians(p2.getLatitude());
        final double lo1 = Math.toRadians(p1.getLongitude());
        final double lo2 = Math.toRadians(p2.getLongitude());

        final double deltaLo = lo2 - lo1;

        final double y = Math.cos(la2) * Math.sin(deltaLo);
        final double x = Math.cos(la1) * Math.sin(la2) - Math.sin(la1) * Math.cos(la2) * Math.cos(deltaLo);

        final double radians = Math.atan2(y, x);
        final float degrees = (float) Math.toDegrees(radians);

        return (degrees + 360f) % 360f;
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
