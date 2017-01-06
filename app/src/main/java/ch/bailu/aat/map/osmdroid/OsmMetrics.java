package ch.bailu.aat.map.osmdroid;

import android.graphics.Rect;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBoxOsm;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.map.MapDistances;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.util.graphic.Pixel;
import ch.bailu.aat.util.ui.AppDensity;

public class OsmMetrics implements MapMetrics {

    private int zoom;
    private final AppDensity density;
    public final Rect screen = new Rect();
    public final Pixel centerPixel = new Pixel();

    private final Pixel cachedPixel = new Pixel();
    private final Rect cachedRect=new Rect();
    private final GeoPoint cachedPoint = new GeoPoint(0,0);

    private MapView.Projection projection;
    private BoundingBoxE6 boundingE6;
    private BoundingBox bounding;

    private final MapDistances distances = new MapDistances();



    public OsmMetrics(AppDensity d, MapView map) {
        density = d;
        init(map);
    }

    public void init(MapView map) {
        map.getScreenRect(screen);

        zoom = map.getZoomLevel();
        centerPixel.x=screen.centerX();
        centerPixel.y=screen.centerY();

        projection = map.getProjection();

        final BoundingBoxOsm boundingBoxOsm = map.getBoundingBox();
        boundingE6 = new BoundingBoxE6(boundingBoxOsm);
        bounding = boundingE6.toBoundingBox();


        distances.init(boundingBoxOsm, screen);

    }



    @Override
    public AppDensity getDensity() {
        return density;
    }

    @Override
    public int getLeft() {
        return screen.left;
    }

    @Override
    public int getRight() {
        return screen.right;
    }

    @Override
    public int getTop() {
        return screen.top;
    }

    @Override
    public int getBottom() {
        return screen.bottom;
    }

    @Override
    public int getWidth() {
        return screen.width();
    }

    @Override
    public int getHeight() {
        return screen.height();
    }


    @Override
    public float pixelToDistance(int pixel) {
        return distances.toDistance(pixel);
    }


    @Override
    public int distanceToPixel(float meter) {
        return (int)distances.toPixel(meter);
    }


    @Override
    public int getShortDistance() {
        return (int)distances.getShortDistance();
    }


    @Override
    public Pixel getCenterPixel() {
        return centerPixel;
    }



    @Override
    public Pixel toPixel(IGeoPoint tp) {
        projection.toMapPixels(tp, cachedPixel);
        return cachedPixel;
    }

    @Override
    public Pixel toPixel(LatLong p) {
        return toPixel(new GeoPoint(p.getLatitudeE6(), p.getLongitudeE6()));

    }

    @Override
    public LatLong fromPixel(int x, int y) {
        return new LatLongE6(fromPixels(x,y)).toLatLong();
    }


    private IGeoPoint fromPixels(int x, int y) {
        return projection.fromPixels(x, y);
    }


    @Override
    public boolean isVisible(IGeoPoint point) {
        return boundingE6.contains(point);
    }


    @Override
    public boolean isVisible(BoundingBoxE6 b) {
        return BoundingBoxE6.doOverlap(b, boundingE6);
    }


    @Override
    public Rect toMapPixels(BoundingBoxE6 b) {
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


    @Override
    public BoundingBox getBoundingBox() {
        return bounding;
    }

    @Override
    public int getZoomLevel() {
        return zoom;
    }

}
