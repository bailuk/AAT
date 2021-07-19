package ch.bailu.aat_lib.coordinates;

import org.mapsforge.core.model.LatLong;

public abstract class MeterCoordinates extends Coordinates {
    /**
     *
     * @return northing part of coordinate in meters
     */
    public abstract int getNorthing();


    /**
     *
     * @return easting part of coordinate in meters
     */
    public abstract int getEasting();

    /**
     *
     * @return WGS84 Latitude / Longitude representation of coordinate
     */
    public abstract LatLong toLatLong();

    /**
     * round northing and easting to decimal place
     * @param dec decimal place to round to
     */
    public abstract void round(int dec);

    public static int round(int v, int dec) {
        return dec * Math.round(((float)v)/((float)dec));
    }



}
