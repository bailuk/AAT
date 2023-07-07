package ch.bailu.aat_lib.coordinates;


/**
 * Interface to an object that stores geo location
 */
public interface LatLongInterface {

    int getLatitudeE6();
    int getLongitudeE6();

    double getLatitude();
    double getLongitude();
}
