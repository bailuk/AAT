package ch.bailu.aat.coordinates;

import org.mapsforge.core.model.LatLong;

public abstract class MeterCoordinates extends Coordinates {
    public abstract int getNorthing();
    public abstract int getEasting();
    //public abstract GeoPoint toLatLongE6();
    public abstract LatLong toLatLong();
    public abstract void round(int c);
    
    public static int round(int v, int c) {
        return c * Math.round(((float)v)/((float)c)); 
    }


}
