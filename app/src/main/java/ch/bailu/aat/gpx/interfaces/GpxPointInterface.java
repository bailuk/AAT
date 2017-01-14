package ch.bailu.aat.gpx.interfaces;

import ch.bailu.aat.coordinates.LatLongE6Interface;
import ch.bailu.aat.gpx.GpxAttributes;

public interface GpxPointInterface extends LatLongE6Interface {
    short getAltitude();
    double getLongitude();
    double getLatitude();
    long getTimeStamp();
    GpxAttributes getAttributes();
}
