package ch.bailu.aat_lib.map;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.util.MapPositionUtil;
import org.mapsforge.map.view.MapView;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.coordinates.LatLongInterface;

public final class MapsForgeMetrics implements MapMetrics {

    private org.mapsforge.core.model.Point tl;
    private byte zoom = 0;
    private Dimension dim;
    private Point center;
    private BoundingBox bounding;


    private final MapView mapView;
    private final AppDensity density;
    private final MapDistances distances = new MapDistances();


    private final int tileSize;


    public MapsForgeMetrics(MapView v, AppDensity d) {
        density = d;
        mapView = v;
        bounding = v.getBoundingBox();
        tileSize = mapView.getModel().displayModel.getTileSize();
    }


    public void init(BoundingBox b, byte z, Dimension d, org.mapsforge.core.model.Point p) {
        dim = d;
        bounding = b;
        tl = p;
        zoom=z;
        distances.init(bounding, dim);
        center = new Point(dim.width/2, dim.height/2);
    }


    public void init(Dimension d) {
        MapPosition pos = mapView.getModel().mapViewPosition.getMapPosition();
        init(   MapPositionUtil.getBoundingBox(pos, d, tileSize),
                mapView.getModel().mapViewPosition.getZoomLevel(),
                d,
                MapPositionUtil.getTopLeftPoint(pos, d, tileSize));
    }



    @Override
    public AppDensity getDensity() {
        return density;
    }

    public int getLeft() {
        return 0;
    }
    public int getRight() {
        return dim.width;
    }
    public int getTop() {
        return 0;
    }
    public int getBottom() {
        return dim.height;
    }
    public int getWidth() {  return dim.width; }
    public int getHeight() { return dim.height; }

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
    public Point getCenterPixel() {
        return center;
    }
    @Override
    public boolean isVisible(BoundingBoxE6 box) {
        return BoundingBoxE6.doOverlap(box, new BoundingBoxE6(bounding));
    }
    @Override
    public boolean isVisible(LatLongInterface point) {
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
    public Point toPixel(LatLongInterface tp) {
        return toPixel(LatLongE6.toLatLong(tp));
    }


    @Override
    public Point toPixel(LatLong p) {
        double y = MercatorProjection.latitudeToPixelY(p.getLatitude(), zoom, tileSize);
        double x = MercatorProjection.longitudeToPixelX(p.getLongitude(), zoom, tileSize);


        return new Point(x-tl.x, y-tl.y);
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
