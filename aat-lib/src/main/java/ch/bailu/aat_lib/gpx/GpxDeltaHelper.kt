package ch.bailu.aat_lib.gpx;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;

import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaPointInterface;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;

public class GpxDeltaHelper  {
    public static float getDistance(GpxPointInterface a, GpxPointInterface b) {

        return getDistance(
                new LatLong(a.getLatitude(), a.getLongitude()),
                new LatLong(b.getLatitude(), b.getLongitude()));


    }

    public static float getDistance(LatLong a, LatLong b) {
        return (float) LatLongUtils.sphericalDistance(a, b);
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
}
