package ch.bailu.aat.coordinates;

import org.osmdroid.util.GeoPoint;

public abstract class MeterCoordinates extends Coordinates {
    public abstract int getNorthing();
    public abstract int getEasting();
    public abstract GeoPoint toGeoPoint();
    public abstract void round(int c);
    
    public static int round(int v, int c) {
        return c * Math.round(((float)v)/((float)c)); 
    }
}
