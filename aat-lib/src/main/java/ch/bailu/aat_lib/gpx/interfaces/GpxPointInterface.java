package ch.bailu.aat_lib.gpx.interfaces;

import ch.bailu.aat_lib.coordinates.LatLongE6Interface;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;

public interface GpxPointInterface extends LatLongE6Interface {
    double getAltitude();
    double getLongitude();
    double getLatitude();
    long getTimeStamp();
    GpxAttributes getAttributes();
}

