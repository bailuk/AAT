package ch.bailu.aat.gpx;

import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.interfaces.GpxDeltaPointInterface;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;



public class GpxDeltaHelper  {
//    private static float rvalue_distance[] = new float[1];

    public static float getDistance(GpxPointInterface a, GpxPointInterface b) {
    	
        return GeoPoint.distanceBetween(a.getLatitude(), a.getLongitude(), 
                b.getLatitude(), b.getLongitude());

        
    }
    
    public static double getBearing(GpxPointInterface a, GpxPointInterface b) {
        GeoPoint p = new GeoPoint(a);
        return p.bearingTo(b);
    }

    
    public static float getAcceleration(GpxDeltaPointInterface a, GpxDeltaPointInterface b) {
        float deltaSpeed=b.getSpeed()-a.getSpeed();
        float deltaTime=getTimeDeltaSI(a,b);
        return getAcceleration(deltaSpeed, deltaTime);
    }
    
    private static float getAcceleration(float deltaSpeed, float deltaTime) {
        if (deltaTime != 0f) return deltaSpeed / deltaTime;
        else return 0f;
    }
    
    public static float getSpeed(GpxPointInterface a, GpxPointInterface b) {
        return getSpeed(getDistance(a,b),getTimeDeltaSI(a,b));
    }
    
    public static float getSpeed(float distance, float time) {
        if (time > 0f) return (distance / time);
        else return 0f;
    }

    public static long getTimeDeltaMilli(GpxPointInterface a, GpxPointInterface b) {
        return b.getTimeStamp() - a.getTimeStamp();
    }
    
    public static float getTimeDeltaSI(GpxPointInterface a, GpxPointInterface b) {
        float deltaT = getTimeDeltaMilli(a,b);
        return deltaT / 1000f;
    }

    public static BoundingBox getBoundingBoxE6(GpxPointInterface a, GpxPointInterface b) {
        return new BoundingBox(a, b);
    }
    
}
