package ch.bailu.aat_lib.gpx.interfaces;

import ch.bailu.aat_lib.coordinates.LatLongInterface;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;

public interface GpxPointInterface extends LatLongInterface {
    double getAltitude();
    double getLongitude();
    double getLatitude();
    long getTimeStamp();
    GpxAttributes getAttributes();
}

