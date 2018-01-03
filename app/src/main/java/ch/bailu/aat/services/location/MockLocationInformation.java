package ch.bailu.aat.services.location;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.util_java.foc.Foc;

public class MockLocationInformation extends LocationInformation {

    private final long time = System.currentTimeMillis();
    private final GpxPointNode node;

    private final Foc file;
    private final int state;

    public MockLocationInformation(Foc f, int s, GpxPointNode n) {
        setVisibleTrackPoint(n);
        node = n;
        file = f;
        state = s;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public Foc getFile() {
        return file;
    }
    @Override
    public long getTimeStamp() {
        return time;
    }
    @Override
    public float getDistance() {
        return node.getDistance();
    }
    @Override
    public float getSpeed() {
        return node.getSpeed();
    }
    @Override
    public float getAcceleration() {
        return node.getAcceleration();
    }
    @Override
    public long getTimeDelta() {
        return node.getTimeDelta();
    }
    @Override
    public BoundingBoxE6 getBoundingBox() {
        return node.getBoundingBox();
    }
    @Override
    public boolean hasAccuracy() {
        return true;
    }
    @Override
    public boolean hasSpeed() {
        return true;
    }
    @Override
    public boolean hasAltitude() {
        return true;
    }
    @Override
    public boolean hasBearing() {
        return true;
    }

    @Override
    public boolean isFromGPS() {
        return true;
    }

    @Override
    public long getCreationTime() {
        return getTimeStamp();
    }

    @Override
    public float getAccuracy() {
        return 5f;
    }

}