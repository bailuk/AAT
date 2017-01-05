package ch.bailu.aat.map.mapsforge.context;

import android.graphics.Point;
import android.graphics.Rect;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.view.MapView;
import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.map.MapDistances;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.graphic.Pixel;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeMetrics implements MapMetrics {

    private final MapView mapView;
    private int l,r,b,t, centerX, centerY, w, h;

    private BoundingBox bounding;

    private final AppDensity density;

    private MapDistances distances = new MapDistances();
    public MapsForgeMetrics(ServiceContext sc, MapView v) {
        bounding = v.getBoundingBox();
        density = new AppDensity(sc);
        mapView = v;
    }

    public void init(BoundingBox boundingBox, Canvas canvas) {
        bounding = boundingBox;
        distances.init(boundingBox, canvas);
    }


    public void init(MapView mapView, Canvas canvas) {

        w=mapView.getWidth();
        h=mapView.getHeight();

        l=(canvas.getWidth() - w) / 2;
        t=(canvas.getHeight() - h) / 2;

        r = l+w;
        b = t+h;

        centerX=canvas.getWidth()/2;
        centerY=canvas.getHeight()/2;

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
        return new Pixel(centerX, centerY);//toPixel(bounding.getCenterPoint());
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
        org.mapsforge.core.model.Point doublePoint = _toPixel(p);
        return new Pixel(l+(int)doublePoint.x, t+(int)doublePoint.y);
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
        return mapView.getModel().mapViewPosition.getZoomLevel();
    }
}
