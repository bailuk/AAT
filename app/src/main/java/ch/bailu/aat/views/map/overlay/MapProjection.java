package ch.bailu.aat.views.map.overlay;

import android.graphics.Point;
import android.graphics.Rect;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;

public class MapProjection {
    public final Rect screen = new Rect();
    public final Point centerPixel = new Point();
    
    private final Point cachedPixel = new Point();
    private final Rect cachedRect=new Rect();
    private final GeoPoint cachedPoint = new GeoPoint(0,0);
    
    private MapView.Projection projection;
    private BoundingBox bounding;

    private float pixels_per_meter;
    private int sdistance;
    private int spixels;

    private final GeoPoint
    nw=new GeoPoint(0,0), 
    sw=new GeoPoint(0,0), 
    ne=new GeoPoint(0,0);


    public void init(MapView map) {
        map.getScreenRect(screen);
        
        
        centerPixel.x=screen.centerX();
        centerPixel.y=screen.centerY();
        
        projection = map.getProjection();

        final BoundingBoxE6 boundingE6 = map.getBoundingBox();
        bounding = new BoundingBox(boundingE6);



        nw.setCoordsE6(boundingE6.getLatNorthE6(), boundingE6.getLonWestE6());

        if (screen.width() < screen.height()) {
            ne.setCoordsE6(boundingE6.getLatNorthE6(), boundingE6.getLonEastE6());
            sdistance = nw.distanceTo(ne);
            spixels = screen.width();

        } else {
            sw.setCoordsE6(boundingE6.getLatSouthE6(), boundingE6.getLonWestE6());
            sdistance = nw.distanceTo(sw);
            spixels = screen.height();
        }
        
        pixels_per_meter = ((float)spixels) / ((float)sdistance);
    }



    public int getPixelFromDistance(float meter) {
        return (int) (meter*pixels_per_meter);
    }

    public int getShortPixels() {
        return spixels;
    }

    public int getShortDistance() {
        return sdistance;
    }


    public float getDistanceFromPixel(float p) {
        return  (p/pixels_per_meter);
    }


    public Rect getScreenRect() {
        return screen;
    }

    public Point getCenterPixel() {
        return centerPixel;
    }


    public Point toMapPixels(IGeoPoint center) {
        return projection.toMapPixels(center, cachedPixel);
    }


    public Point toPixels(GpxPointInterface tp, Point pixel) {
        return projection.toMapPixels(tp, pixel);
    }


    public boolean isVisible(GpxPointInterface point) {
        return bounding.contains(point);
    }


    public boolean isVisible(BoundingBox b) {
        return BoundingBox.doOverlap(b, bounding);
    }


    public Rect toMapPixels(BoundingBox b) {
        cachedPoint.setCoordsE6(b.getLatNorthE6(), b.getLonWestE6());
        
        projection.toMapPixels(cachedPoint, cachedPixel);
        cachedRect.left = cachedPixel.x;
        cachedRect.top = cachedPixel.y;

        
        cachedPoint.setCoordsE6(b.getLatSouthE6(), b.getLonEastE6());
        
        projection.toMapPixels(cachedPoint, cachedPixel);
        cachedRect.right = cachedPixel.x;
        cachedRect.bottom = cachedPixel.y;

        return cachedRect;
    }


    public IGeoPoint fromPixels(int x, int y) {
        return projection.fromPixels(x, y);
    }


    public GeoPoint getCenterPoint() {
        cachedPoint.setCoordsE6(
               (bounding.getLatNorthE6() + bounding.getLatSouthE6()) / 2,
               (bounding.getLonEastE6() + bounding.getLonWestE6()) / 2);
        return cachedPoint;
    }
}
