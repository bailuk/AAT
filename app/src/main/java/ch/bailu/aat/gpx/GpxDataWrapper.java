package ch.bailu.aat.gpx;


import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.gpx.interfaces.GpxDeltaPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxType;


public class GpxDataWrapper implements GpxDeltaPointInterface, GpxBigDeltaInterface {

    private GpxBigDeltaInterface summary;
    private GpxPointInterface point;

    public GpxDataWrapper() {
        setVisibleTrackSegment(GpxBigDelta.NULL);
        setVisibleTrackPoint(GpxPoint.NULL);
    }

    public void setVisibleTrackSegment(GpxBigDeltaInterface s) {
        summary = s;
    }

    public void setVisibleTrackPoint(GpxPointInterface p) {
        point = p;
    }


    @Override
    public int getLatitudeE6() {
        return point.getLatitudeE6();
    }

    @Override
    public int getLongitudeE6() {
        return point.getLongitudeE6();
    }
    
    @Override
    public float getSpeed() {
        return summary.getSpeed();
    }

    @Override
    public float getDistance() {
        return summary.getDistance();
    }

    @Override
    public long getStartTime() {
        return summary.getStartTime();
    }

    @Override
    public long getTimeDelta() {
        return summary.getTimeDelta();
    }


    @Override
    public long getPause()  {
        return summary.getPause();
    }

    @Override
    public short getAltitude() {
        return point.getAltitude();
    }

    @Override
    public double getLatitude() {
        return point.getLatitude();
    }

    @Override
    public double getLongitude() {
        return point.getLongitude();
    }

    @Override
    public long getTimeStamp() {
        return point.getTimeStamp();
    }

    @Override
    public float getAcceleration() {
        return summary.getAcceleration();
    }


    @Override
    public BoundingBoxE6 getBoundingBox() {
        return summary.getBoundingBox();
    }

    @Override
    public long getEndTime() {
        return summary.getEndTime();
    }



    @Override
    public GpxType getType() {
        return summary.getType();
    }

    @Override
    public GpxAttributes getAttributes() {
        return point.getAttributes();
    }
}
