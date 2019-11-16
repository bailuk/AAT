package ch.bailu.aat.gpx;

import android.location.Location;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.LatLongE6Interface;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;


public class GpxPoint implements GpxPointInterface {
    public static final long SIZE_IN_BYTES=(1*4)+(2*4)+(1*8);

    private float  altitude;
    private final int    longitude;
    private final int latitude;
    private final long   timestamp;


    public static final GpxPoint NULL=new GpxPoint();

    private GpxPoint () {
        altitude = 0;
        longitude = 0;
        latitude = 0;
        timestamp = 0;
    }


    public GpxPoint (GpxPointInterface tp) {
        altitude= (float) tp.getAltitude();
        longitude=tp.getLongitudeE6();
        latitude=tp.getLatitudeE6();
        timestamp=tp.getTimeStamp();


    }

    public GpxPoint (Location location) {
        altitude=(float)location.getAltitude();
        longitude=(int) (location.getLongitude()*1E6);
        latitude=(int) (location.getLatitude()*1E6);
        timestamp=location.getTime();
    }

    public GpxPoint(LatLong p, float a, long time) {
        latitude=p.getLatitudeE6();
        longitude=p.getLongitudeE6();
        altitude= a;
        timestamp=time;
    }

    public GpxPoint(LatLongE6Interface gp, float a, long time) {
        latitude = gp.getLatitudeE6();
        longitude = gp.getLongitudeE6();
        altitude = a;
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
    public double getAltitude() {
        return (double)altitude;
    }


    public void setAltitude(float e) {
        altitude=e;
    }


    @Override
    public GpxAttributes getAttributes() {
        return GpxAttributes.NULL;
    }


}

