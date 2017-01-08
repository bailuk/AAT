package ch.bailu.aat.map.mapsforge;

import android.graphics.Point;
import android.graphics.Rect;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.android.view.MapView;
import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.map.MapDistances;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.graphic.Pixel;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppLog;

public class MapsForgeMetrics implements MapMetrics {

    private org.mapsforge.core.model.Point tl;
    private byte zoom = 0;

    private final MapView mapView;
    private int l,r,b,t, w, h;
    private Pixel center;


    private BoundingBox bounding;

    private final AppDensity density;

    private final MapDistances distances = new MapDistances();



    public MapsForgeMetrics(ServiceContext sc, MapView v) {
        bounding = v.getBoundingBox();
        density = new AppDensity(sc);
        mapView = v;
    }



    public void init(BoundingBox bb, byte z, Canvas c, org.mapsforge.core.model.Point tlp) {
        zoom = z;
        bounding = bb;
        distances.init(bb, c);

        tl = tlp;

        w=mapView.getWidth();
        h=mapView.getHeight();
        center = new Pixel(c.getWidth()/2, c.getHeight()/2);//toPixel(bounding.getCenterPoint());

        final int hw = w / 2;
        final int hh = h / 2;

        l = center.x - hw;
        r = center.y + hw;
        b = center.x + hh;
        t = center.y - hh;
    }



    @Override
    public AppDensity getDensity() {
        return density;
    }

    public int getLeft() {
        return l;
    }
    public int getRight() {
        return r;
    }
    public int getTop() {
        return t;
    }
    public int getBottom() {
        return b;
    }
    public int getWidth() {  return w; }
    public int getHeight() { return h; }

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


    private org.mapsforge.core.model.Point _toPixel(LatLong p) {
        return mapView.getMapViewProjection().toPixels(p);
    }
    @Override
    public Pixel getCenterPixel() {
        return center;
    }
    @Override
    public boolean isVisible(BoundingBoxE6 box) {
        return BoundingBoxE6.doOverlap(box, new BoundingBoxE6(bounding));
    }
    @Override
    public boolean isVisible(IGeoPoint point) {
        return bounding.contains(
                LatLongE6.toD(point.getLatitudeE6()),
                LatLongE6.toD(point.getLongitudeE6()));
    }

    @Override
    public Rect toMapPixels(BoundingBoxE6 box) {
        Rect rect = new Rect();

        Point tl = toPixel(new LatLongE6(box.getLatNorthE6(), box.getLonWestE6()).toLatLong());
        Point br = toPixel(new LatLongE6(box.getLatSouthE6(), box.getLonEastE6()).toLatLong());
        rect.left = tl.x;
        rect.right = br.x;
        rect.bottom = br.y;
        rect.top = tl.y;
        return rect;
    }
    @Override
    public Pixel toPixel(IGeoPoint tp) {
        return toPixel(new LatLongE6(tp).toLatLong());
    }

    @Override
    public Pixel toPixel(LatLong p) {
        final byte zoom = (byte)getZoomLevel();
        final int tilesize = mapView.getModel().displayModel.getTileSize();


        double y = MercatorProjection.latitudeToPixelY(p.getLatitude(), zoom, tilesize);
        double x = MercatorProjection.longitudeToPixelX(p.getLongitude(), zoom, tilesize);


        return new Pixel((int)(x-tl.x), (int)(y-tl.y));
    }

    @Override
    public LatLong fromPixel(int x, int y) {
       return mapView.getMapViewProjection().fromPixels(x, y);
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
