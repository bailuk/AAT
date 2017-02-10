package ch.bailu.aat.coordinates;

import org.mapsforge.core.model.LatLong;

public abstract class Coordinates {
    public abstract String toString();


    public static LatLong stringToGeoPoint(String src) throws NumberFormatException{
        String[] parts = src.split("[:,?#]");

        int hit=0;

        double la=0, lo;
        for (int x = 0; x < parts.length && hit < 2; x++) {
            final double d = Double.parseDouble(parts[x]);
            hit++;

            if (hit==1) {
                la = d;
            } else if (hit==2) {
                lo = d;
                if (lo != 0d && la !=0d) {
                    return new LatLong(la, lo);
                }
            }
        }
        throw new NumberFormatException();
    }

    public static String geoPointToGeoUri(LatLong src) {
        return  "geo:" +
                src.getLatitude() +
                ',' +
                src.getLongitude();
    }


    public static String geoPointToDescription(LatLong src) {
        StringBuilder b = new StringBuilder();

        b.append("Coordinates:\nLatitude:");
        b.append(src.getLatitude());
        b.append("Longitude:");
        b.append(src.getLongitude());

        return b.toString();    }
}
