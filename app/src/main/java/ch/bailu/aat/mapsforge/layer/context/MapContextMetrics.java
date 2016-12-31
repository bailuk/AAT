package ch.bailu.aat.mapsforge.layer.context;

import android.graphics.Point;
import android.graphics.Rect;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;
import org.mapsforge.map.android.view.MapView;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.helpers.AppDensity;
import ch.bailu.aat.services.ServiceContext;

public class MapContextMetrics {

    private final MapView mapView;

    private int l,r,b,t, centerX, centerY, w, h;

    private BoundingBox bounding;

    public final AppDensity density;

    private float meterPerOnePixel = 1;
    private float pixelPerOneMeter = 1;
    private int shortDistance = 1;


    public MapContextMetrics(ServiceContext sc, MapView v) {
        density = new AppDensity(sc);
        mapView = v;
    }

    public void init(BoundingBox boundingBox, Canvas canvas) {

        bounding = boundingBox;

        setDistances(boundingBox, canvas);

    }


    private void setDistances(BoundingBox box, Canvas canvas) {
        if (canvas.getHeight() < canvas.getWidth()) {
            LatLong a = new LatLong(box.minLatitude, box.maxLongitude);
            LatLong b = new LatLong(box.maxLatitude, box.maxLongitude);

            setDistances(a,b, canvas.getHeight());
        } else {
            LatLong a = new LatLong(box.maxLatitude, box.minLongitude);
            LatLong b = new LatLong(box.maxLatitude, box.maxLongitude);

            setDistances(a,b, canvas.getWidth());
        }
    }

    private void setDistances(LatLong a, LatLong b, float pixel) {
        float meter = (float)LatLongUtils.sphericalDistance(a, b);
        meterPerOnePixel = meter / pixel;
        pixelPerOneMeter = pixel / meter;

        shortDistance = (int) this.pixelToDistance(w);
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


    public float pixelToDistance(int pixel) {
        return pixel * meterPerOnePixel;
    }
    public int distanceToPixel(float meter) {
        return (int)(meter * pixelPerOneMeter);
    }


    public int getShortDistance() {
        return shortDistance;
    }





    private org.mapsforge.core.model.Point _toPixel(LatLong p) {
        return mapView.getMapViewProjection().toPixels(p);
    }

    public Point getCenterPixel() {
        return toPixel(bounding.getCenterPoint());
    }

    public boolean isVisible(BoundingBoxE6 box) {
        return BoundingBoxE6.doOverlap(box, new BoundingBoxE6(bounding));
    }

    public boolean isVisible(GpxPointInterface point) {
        return bounding.contains(point.getLatitude(), point.getLongitude());
    }

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

    public Point toPixel(GpxPointInterface tp) {
        return toPixel(new LatLong(tp.getLatitude(), tp.getLongitude()));
    }


    public Point toPixel(LatLong p) {
        org.mapsforge.core.model.Point doublePoint = _toPixel(p);
        return new Point(l+(int)doublePoint.x, t+(int)doublePoint.y);
    }

}
