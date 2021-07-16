package ch.bailu.aat.map;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;

public class MapDistances {


    private float meterPerOnePixel;
    private float pixelPerOneMeter;
    private float shortDistance;



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
