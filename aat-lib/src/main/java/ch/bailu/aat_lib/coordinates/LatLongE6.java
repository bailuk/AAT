package ch.bailu.aat_lib.coordinates;



import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;

import javax.annotation.Nonnull;


public class LatLongE6 implements LatLongE6Interface {
    public final int la, lo;


    public LatLongE6(int latitude, int longitude) {
        la = latitude;
        lo = longitude;
    }


    public LatLongE6(double latitude, double longitude) {
        la = toE6(latitude);
        lo = toE6(longitude);
    }


    public LatLongE6(LatLong p) {
        la = toE6(p.latitude);
        lo = toE6(p.longitude);
    }


    public LatLong toLatLong() {
        return new LatLong(toD(la),toD(lo));
    }

    public static double toD(int in) {
        return LatLongUtils.microdegreesToDegrees(in);
    }

    public static int toE6(double in) {
        return LatLongUtils.degreesToMicrodegrees(in);
    }

    @Override
    public int getLatitudeE6() {
        return la;
    }

    @Override
    public int getLongitudeE6() {
        return lo;
    }

    public static LatLong toLatLong(LatLongE6Interface tp) {
        return new LatLong(toD(tp.getLatitudeE6()), toD(tp.getLongitudeE6()));

    }

    @Nonnull
    @Override
    public String toString() {
        return toLatLong().toString();
    }
}
