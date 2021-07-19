package ch.bailu.aat_lib.coordinates;


import org.mapsforge.core.model.LatLong;

import java.text.DecimalFormat;

import javax.annotation.Nonnull;


/**
 * Holds a specific geo location. Generates file path and url for the DEM3 tile
 * that is responsible for this geo location.
 * <p>
 * Digital Elevation Models in 3 arc / second resolution.
 * All tiles are taken from http://viewfinderpanoramas.org/dem3.html and repackaged.
 * Most tiles are originally from the 2000 Shuttle Radar Topography Mission.
 * See http://viewfinderpanoramas.org/dem3 and https://bailu.ch/dem3 for details.
 */
public class Dem3Coordinates extends Coordinates implements LatLongE6Interface {

    private final static String BASE_URL = "https://bailu.ch/dem3/";


    private final int la, lo;
    private final String string;


    public Dem3Coordinates(int la, int lo) {
        this(la/1e6,lo/1e6);
    }


    public Dem3Coordinates(double la, double lo) {
        this.la=(int)Math.floor(la);
        this.lo=(int)Math.floor(lo);
        string = toLaString() + toLoString();
    }



    public Dem3Coordinates(LatLongE6Interface p) {
        this(p.getLatitudeE6(), p.getLongitudeE6());
    }

    public Dem3Coordinates(LatLong p) {
        this(p.getLatitude(), p.getLongitude());
    }


    private final static DecimalFormat f2 = new DecimalFormat("00");
    private final static DecimalFormat f3 = new DecimalFormat("000");

    public String toLaString() {
        return WGS84Coordinates.getLatitudeChar(la) + f2.format(Math.abs(la));
    }


    public String toLoString() {
        return WGS84Coordinates.getLongitudeChar(lo) + f3.format(Math.abs(lo));
    }

    /**
     *
     * @return A base string for a file name of a dem3 tile. Example: "N16E077"
     */
    @Nonnull
    @Override
    public String toString() {
        return string;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Dem3Coordinates &&
                string.equals(obj.toString());


    }


    @Override
    public int hashCode() {
        return string.hashCode();
    }

    /**
     *
     * @return A base string for a file name and directory of a dem3 tile. Example: "N16/N16E077"
     */
    public String toExtString() {
        return toLaString() + "/" + toString();
    }

    /**
     *
     * @return URL of dem3 tile. Example: https://bailu.ch/dem3/N16/N16E077.hgt.zip
     */
    public String toURL() {
        return (BASE_URL + toExtString() + ".hgt.zip");
    }


    /**
     *
     * @return exact coordinates
     */
    @Override
    public LatLong toLatLong() {
        return new LatLong(la,lo);
    }

    @Override
    public int getLatitudeE6() {
        return la * (int)1e6;
    }


    @Override
    public int getLongitudeE6() {
        return lo * (int)1e6;
    }

}
