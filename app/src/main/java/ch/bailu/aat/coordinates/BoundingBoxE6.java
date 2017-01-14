package ch.bailu.aat.coordinates;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat.gpx.parser.DoubleParser;
import ch.bailu.aat.gpx.parser.SimpleStream;

public class BoundingBoxE6 {


    public final static BoundingBoxE6 NULL_BOX = new BoundingBoxE6(0,0);


    private int north = Integer.MIN_VALUE,
            east  = Integer.MIN_VALUE,
            south = Integer.MAX_VALUE,
            west  = Integer.MAX_VALUE;



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

//    public BoundingBoxE6(GpxPointInterface a, GpxPointInterface b) {
//        add(a.getLatitudeE6(), a.getLongitudeE6(), b.getLatitudeE6(), b.getLongitudeE6());
//    }
//
//
//    public BoundingBoxE6(BoundingBoxOsm b) {
//        add(b);
//    }


    public void add(String bounding) {
        final SimpleStream stream = new SimpleStream(bounding);
        final DoubleParser parser = new DoubleParser(stream,6);

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

//    public void add(BoundingBoxOsm b) {
//        add(b.getLatNorthE6(), b.getLonEastE6(),
//            b.getLatSouthE6(), b.getLonWestE6());
//    }


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
        return new BoundingBox(
                LatLongE6.toD(Math.min(south, north)),
                LatLongE6.toD(Math.min(west, east)),
                LatLongE6.toD(Math.max(south, north)),
                LatLongE6.toD(Math.max(west, east)));
    }

//    public BoundingBoxOsm toBoundingBoxE6() {
//        return new BoundingBoxOsm(north, east, south, west);
//    }


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
        return String.format((Locale) null,
                "%.2f,%.2f,%.2f,%.2f",
                north/1e6f, west/1e6f, south/1e6f, east/1e6f);

    }


    public int getLatitudeSpanE6() {
        return Math.abs(north - south);
    }

    public int getLongitudeSpanE6() {
        return Math.abs(west - east);
    }

    public LatLongE6 getCenter() {
        return new LatLongE6(south + getLatitudeSpanE6(), west + getLongitudeSpanE6());
    }
}

