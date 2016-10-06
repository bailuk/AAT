package ch.bailu.aat.coordinates;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBoxE6;

import java.io.IOException;
import java.util.Locale;

import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.gpx.parser.DoubleParser;
import ch.bailu.aat.gpx.parser.SimpleStream;

public class BoundingBox {

    
    public final static BoundingBox NULL_BOX = new BoundingBox(0,0);

    
    private int north = Integer.MIN_VALUE, 
                east  = Integer.MIN_VALUE, 
                south = Integer.MAX_VALUE, 
                west  = Integer.MAX_VALUE;
    
    
    public BoundingBox() {}
    
    public BoundingBox(int n, int e, int s, int w) {
        add(n,e,s,w);
    }

    
    public BoundingBox(int la, int lo) {
        add(la, lo);
    }

    
    
    public BoundingBox(BoundingBox b) {
        add(b);
    }

    public BoundingBox(GpxPointInterface a, GpxPointInterface b) {
        add(a.getLatitudeE6(), a.getLongitudeE6(), b.getLatitudeE6(), b.getLongitudeE6());
    }


    public BoundingBox(BoundingBoxE6 b) {
        add(b);
    }

    
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
    

    public void add(BoundingBox b) {
        add(b.north, b.east, 
            b.south, b.west);
    }


    public void add(IGeoPoint point) {
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

    public void add(BoundingBoxE6 b) {
        add(b.getLatNorthE6(), b.getLonEastE6(), 
            b.getLatSouthE6(), b.getLonWestE6());
    }
    


    public boolean contains(IGeoPoint p) {
        return contains(p.getLatitudeE6(), p.getLongitudeE6());
    }
    
    
    public boolean contains(int la, int lo) {
        return la < north && la > south && lo < east && lo > west;
    }

    public static boolean doOverlap(BoundingBox b1, BoundingBox b2) {
        return
        (b1.containsLatitude(b2) || b2.containsLatitude(b1)) &&
        (b2.containsLongitude(b1) || b1.containsLongitude(b2));
    }

    
    public boolean containsLongitude(BoundingBox b) {
        return containsLongitude(b.east) || containsLongitude(b.west);
    }
    
    public boolean containsLongitude(int lo) {
        return lo > west && lo < east;
    }
    
    public boolean containsLatitude(BoundingBox b) {
        return containsLatitude(b.north) || containsLatitude(b.south);
    }
    
    public boolean containsLatitude(int la) {
        return la < north && la > south;
    }
    
    public BoundingBoxE6 toBoundingBoxE6() {
        return new BoundingBoxE6(north, east, south, west);
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
        return String.format((Locale) null,
                "%.2f,%.2f,%.2f,%.2f",
                north/1e6f, west/1e6f, south/1e6f, east/1e6f);

    }

}

