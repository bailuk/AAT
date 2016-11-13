package ch.bailu.aat.gpx.interfaces;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxAttributesStatic;

public interface GpxPointInterface extends IGeoPoint {
    short getAltitude();
    double getLongitude();
    double getLatitude();
    long getTimeStamp();
    GpxAttributes getAttributes();
}
