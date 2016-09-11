package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.interfaces.GpxDeltaPointInterface;

public class GpxDeltaPoint implements GpxDeltaPointInterface {
    private GpxPoint pointA = GpxPoint.NULL;
    private GpxPoint pointB = GpxPoint.NULL;
    private Updater updater = new PrimaryUpdater();
    
    
    private abstract class Updater {
        public abstract void update(GpxPoint p);
    }
    
    private class SecondaryUpdater extends Updater{
        @Override
        public void update(GpxPoint p) {
            pointA = pointB;
            pointB = p;
        }
    }
    
    private class PrimaryUpdater extends Updater {
        @Override 
        public void update(GpxPoint p) {
            pointA = p;
            pointB = p;
            updater = new SecondaryUpdater();
        }

    }
    
    
    public void update(GpxPoint p) {
        updater.update(p);
    }
    

    @Override
    public float getAcceleration() {
        return 0;
        //return TrackDeltaHelper.getAccelearation(pointA, pointB);
    }


    @Override
    public BoundingBox getBoundingBox() {
        return GpxDeltaHelper.getBoundingBoxE6(pointA, pointB);
    }


    @Override
    public float getDistance() {
        return GpxDeltaHelper.getDistance(pointA, pointB);
    }


    @Override
    public float getSpeed() {
        return GpxDeltaHelper.getSpeed(pointA, pointB);
    }


    @Override
    public long getTimeDelta() {
        return GpxDeltaHelper.getTimeDeltaMilli(pointA, pointB);
    }


    @Override
    public short getAltitude() {
        return pointB.getAltitude();
    }


    @Override
    public double getLatitude() {
        return pointB.getLatitude();
    }


    @Override
    public double getLongitude() {
        return pointB.getLongitude();
    }


    @Override
    public long getTimeStamp() {
        return pointB.getTimeStamp();
    }


    @Override
    public int getLatitudeE6() {
        return pointB.getLatitudeE6();
    }


    @Override
    public int getLongitudeE6() {
        return pointB.getLongitudeE6();
    }


    @Override
    public double getBearing() {
        return GpxDeltaHelper.getBearing(pointA, pointB);
    }


    @Override
    public GpxAttributes getAttributes() {
        return pointB.getAttributes();
    }
}
