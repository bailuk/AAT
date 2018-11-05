package ch.bailu.aat.coordinates;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;

import java.io.IOException;

import ch.bailu.aat.description.FF;
import ch.bailu.util_java.parser.scanner.DoubleScanner;
import ch.bailu.util_java.io.Stream;

public class BoundingBoxE6 {


    private static final int MIN_LA = LatLongE6.toE6(LatLongUtils.LATITUDE_MIN);
    private static final int MAX_LA = LatLongE6.toE6(LatLongUtils.LATITUDE_MAX);
    private static final int MAX_LO = LatLongE6.toE6(LatLongUtils.LONGITUDE_MAX);
    private static final int MIN_LO = LatLongE6.toE6(LatLongUtils.LONGITUDE_MIN);


    public final static BoundingBoxE6 NULL_BOX = new BoundingBoxE6(0,0);


    private int north = MIN_LA,
            east  = MIN_LO,
            south = MAX_LA,
            west  = MAX_LO;




    public BoundingBoxE6() {}

    public BoundingBoxE6(int n, int e, int s, int w) {
        add(n,e,s,w);
    }


    public BoundingBoxE6(int la, int lo) {
        add(la, lo);
    }

    public BoundingBoxE6(BoundingBox b) {
        north = LatLongE6.toE6(b.maxLatitude);
        south = LatLongE6.toE6(b.minLatitude);
        west = LatLongE6.toE6(b.minLongitude);
        east = LatLongE6.toE6(b.maxLongitude);

    }


    public BoundingBoxE6(BoundingBoxE6 b) {
        add(b);
    }



    public void add(String bounding) {
        final Stream stream = new Stream(bounding);
        final DoubleScanner parser = new DoubleScanner(stream,6);

        try {
            parser.scan();
            final int s=parser.getInt();


            parser.scan();
            final int n=parser.getInt();


            parser.scan();
            final int w=parser.getInt();

            parser.scan();
            final int e=parser.getInt();

            add(n,e,s,w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void add(BoundingBoxE6 b) {
        add(b.north, b.east,
                b.south, b.west);
    }



    public void add(LatLong latLong) {
        add(latLong.getLatitudeE6(), latLong.getLongitudeE6());
    }

    public void add(LatLongE6Interface point) {
        add(point.getLatitudeE6(), point.getLongitudeE6());
    }



    public void add(int la, int lo) {
        add(la, lo, la, lo);
    }

    public void add(int n, int e, int s, int w) {
        north = Math.max(n,north);
        east  = Math.max(e,east);
        south = Math.min(s,south);
        west  = Math.min(w,west);
    }


    public boolean contains(LatLong p) {
        return contains(p.getLatitudeE6(), p.getLongitudeE6());
    }

    public boolean contains(LatLongE6Interface p) {
        return contains(p.getLatitudeE6(), p.getLongitudeE6());
    }


    public boolean contains(int la, int lo) {
        return la < north && la > south && lo < east && lo > west;
    }

    public static boolean doOverlap(BoundingBoxE6 b1, BoundingBoxE6 b2) {
        return
                (b1.containsLatitude(b2) || b2.containsLatitude(b1)) &&
                        (b2.containsLongitude(b1) || b1.containsLongitude(b2));
    }


    public boolean containsLongitude(BoundingBoxE6 b) {
        return containsLongitude(b.east) || containsLongitude(b.west);
    }

    public boolean containsLongitude(int lo) {
        return lo > west && lo < east;
    }

    public boolean containsLatitude(BoundingBoxE6 b) {
        return containsLatitude(b.north) || containsLatitude(b.south);
    }

    public boolean containsLatitude(int la) {
        return la < north && la > south;
    }

    public BoundingBox toBoundingBox() {
        validate();
        BoundingBoxE6 b = new BoundingBoxE6(this);
        b.validate();

        return new BoundingBox(
                LatLongE6.toD(Math.min(b.south, b.north)),
                LatLongE6.toD(Math.min(b.west, b.east)),
                LatLongE6.toD(Math.max(b.south, b.north)),
                LatLongE6.toD(Math.max(b.west, b.east)));
    }



    public int getLatNorthE6() {
        return north;
    }

    public int getLonWestE6() {
        return west;
    }

    public int getLonEastE6() {
        return east;
    }

    public int getLatSouthE6() {
        return south;
    }

    public boolean hasBounding() {
        return (north > south && east > west);
    }


    @Override
    public String toString() {
        return  FF.N_2.format(north/1e6f) + "," +
                FF.N_2.format(west/1e6f)     + "," +
                FF.N_2.format(south/1e6f)    + "," +
                FF.N_2.format(east/1e6f);
    }


    public int getLatitudeSpanE6() {
        return Math.abs(north - south);
    }

    public int getLongitudeSpanE6() {
        return Math.abs(west - east);
    }

    public LatLongE6 getCenter() {
        return new LatLongE6(south + getLatitudeSpanE6()/2, west + getLongitudeSpanE6()/2);
    }


    private void validate() {
        north = validate(north, MIN_LA, MAX_LA);
        south = validate(south, MIN_LA, MAX_LA);
        east = validate(east, MIN_LO, MAX_LO);
        west = validate(west, MIN_LO, MAX_LO);

    }

    private static int validate(int val, int min, int max) {
        val = Math.min(val, max);
        val = Math.max(val, min);
        return val;
    }

}

