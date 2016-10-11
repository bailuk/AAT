package ch.bailu.aat.coordinates;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

public abstract class Coordinates {
    public abstract String toString();


    public static boolean stringToGeoPoint(String src, GeoPoint dst) {
        String[] parts = src.split("[:,?#]");

        int hit=0;

        double la=0, lo;
        for (int x = 0; x < parts.length && hit < 2; x++) {
            try {
                final double d = Double.parseDouble(parts[x]);
                hit++;

                if (hit==1) {
                    la = d;
                } else if (hit==2) {
                    lo = d;
                    if (lo != 0d && la !=0d) {
                        dst.setLatitudeE6((int)(la*1E6));
                        dst.setLongitudeE6((int)(lo*1E6));
                        return true;
                    }
                }

            } catch (NumberFormatException  e) {

            }

        }
        return false;
    }

    public static String geoPointToGeoUri(IGeoPoint src) {
        String b = "geo:" +
                src.getLatitudeE6() / 1e6d +
                ',' +
                src.getLongitudeE6() / 1e6d;

        return b;
    }


    public static String geoPointToDescription(GeoPoint src) {
        StringBuilder b = new StringBuilder();

        b.append("Coordinates:\nLatitude:");
        b.append(src.getLatitudeE6()/1e6d);
        b.append("Longitude:");
        b.append(src.getLongitudeE6()/1e6d);

        return b.toString();    }
}
