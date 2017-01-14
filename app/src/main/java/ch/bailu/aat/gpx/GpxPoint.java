package ch.bailu.aat.gpx;

import android.location.Location;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.LatLongE6Interface;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;


public class GpxPoint implements GpxPointInterface {
    public static final long SIZE_IN_BYTES=(1*2)+(2*4)+(1*8);

    private short  altitude;
    private int    longitude, latitude; 
    private long   timestamp;

    
    public static final GpxPoint NULL=new GpxPoint();

    private GpxPoint () {}

    
    public GpxPoint (GpxPointInterface tp) {
        altitude= tp.getAltitude();
        longitude=tp.getLongitudeE6();
        latitude=tp.getLatitudeE6();
        timestamp=tp.getTimeStamp();


    }

    public GpxPoint (Location location) {
        altitude=(short)location.getAltitude();
        longitude=(int) (location.getLongitude()*1E6);
        latitude=(int) (location.getLatitude()*1E6);
        timestamp=location.getTime();
    }

    public GpxPoint(LatLong p, int a, long time) {
        latitude=p.getLatitudeE6();
        longitude=p.getLongitudeE6();
        altitude=(short) a;
        timestamp=time;
    }

    public GpxPoint(LatLongE6Interface gp, int a, long time) {
        latitude = gp.getLatitudeE6();
        longitude = gp.getLongitudeE6();
        altitude = (short)a;
        timestamp = time; 
    }

    @Override
    public double getLatitude() {
        return ((double)getLatitudeE6())/ 1E6;
    }

    @Override
    public double getLongitude() {
        return ((double)getLongitudeE6())/ 1E6;
    }

    @Override
    public long getTimeStamp() {
        return timestamp;
    }

    @Override
    public int getLatitudeE6() {
        return latitude;
    }

    @Override
    public int getLongitudeE6() {
        return longitude;
    }

    @Override
    public short getAltitude() {
        return altitude;
    }


    public void setAltitude(short e) {
        altitude=e;
    }


    @Override
    public GpxAttributes getAttributes() {
        return GpxAttributesStatic.NULL_ATTRIBUTES;
    }


}

