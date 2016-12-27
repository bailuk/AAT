package ch.bailu.aat.gpx.interfaces;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.gpx.GpxAttributes;

public interface GpxPointInterface extends IGeoPoint {
    short getAltitude();
    double getLongitude();
    double getLatitude();
    long getTimeStamp();
    GpxAttributes getAttributes();
}
