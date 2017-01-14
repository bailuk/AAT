package ch.bailu.aat.map;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;

public class MapDistances {


    private float meterPerOnePixel;
    private float pixelPerOneMeter;
    private float shortDistance;


//    private final GeoPoint
//            nw=new GeoPoint(0,0),
//            sw=new GeoPoint(0,0),
//            ne=new GeoPoint(0,0);
//
//    public void init(BoundingBoxOsm boundingBoxOsm, Rect screen) {
//        float meter, pixel;
//
//        nw.setCoordsE6(boundingBoxOsm.getLatNorthE6(), boundingBoxOsm.getLonWestE6());
//
//        if (screen.width() < screen.height()) {
//            ne.setCoordsE6(boundingBoxOsm.getLatNorthE6(), boundingBoxOsm.getLonEastE6());
//            meter = nw.distanceTo(ne);
//            pixel = screen.width();
//
//        } else {
//            sw.setCoordsE6(boundingBoxOsm.getLatSouthE6(), boundingBoxOsm.getLonWestE6());
//            meter = nw.distanceTo(sw);
//            pixel = screen.height();
//        }
//
//        meterPerOnePixel = meter / pixel;
//        pixelPerOneMeter = pixel / meter;
//
//        shortDistance = meter;
//    }

    public void init(BoundingBox box, Dimension dim) {
        if (dim.height < dim.width) {
            LatLong a = new LatLong(box.minLatitude, box.maxLongitude);
            LatLong b = new LatLong(box.maxLatitude, box.maxLongitude);

            set(a,b, dim.height);
        } else {
            LatLong a = new LatLong(box.maxLatitude, box.minLongitude);
            LatLong b = new LatLong(box.maxLatitude, box.maxLongitude);

            set(a,b, dim.width);
        }
    }

    private void set(LatLong a, LatLong b, float pixel) {
            float meter = (float) LatLongUtils.sphericalDistance(a, b);
            meterPerOnePixel = meter / pixel;
            pixelPerOneMeter = pixel / meter;

            shortDistance = meter;

    }


    public float toDistance(float pixel) {
        return pixel * meterPerOnePixel;
    }
    public float toPixel(float meter) {
        return meter * pixelPerOneMeter;
    }


    public float getShortDistance() {
        return shortDistance;
    }



}
