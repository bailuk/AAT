package ch.bailu.aat.coordinates;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

public abstract class Coordinates {
    public abstract String toString();


    public static boolean stringToGeoPoint(String src, GeoPoint dst) {
        String[] parts = src.split("[:,?#]");

        int c=0;

        double la=0, lo;
        for (int x = 0; x < parts.length; x++) {
            try {
                final double d = Double.parseDouble(parts[x]);
                c++;

                if (c==1) {
                    la = d;
                } else if (c==2) {
                    lo = d;

                    dst.setLatitudeE6((int)(la*1E6));
                    dst.setLongitudeE6((int)(lo*1E6));
                    return true;
                }

            } catch (NumberFormatException  e) {

            }

        }
        return false;
    }

    public static String geoPointToGeoUri(IGeoPoint src) {
        StringBuilder b = new StringBuilder();
        
        b.append("geo:");
        b.append(src.getLatitudeE6()/1e6d);
        b.append(',');
        b.append(src.getLongitudeE6()/1e6d);
        
        return b.toString();
    }


    public static String geoPointToDescription(GeoPoint src) {
        StringBuilder b = new StringBuilder();
        
        b.append("Coordinates:\nLatitude:");
        b.append(src.getLatitudeE6()/1e6d);
        b.append("Longitude:");
        b.append(src.getLongitudeE6()/1e6d);
        
        return b.toString();    }
}
