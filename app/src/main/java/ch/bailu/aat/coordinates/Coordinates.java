package ch.bailu.aat.coordinates;

import android.support.annotation.NonNull;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.util.ui.AppLog;

public abstract class Coordinates {
    @NonNull
    public abstract String toString();


    public static LatLong stringToGeoPoint(String src) throws NumberFormatException{
        String[] parts = src.split("[:,?#]");

        boolean scanLa=true;

        double la=0;
        for (String p : parts) {
            try  {
                final double d = Double.parseDouble(p);
                if (scanLa) {
                    la = d;
                    scanLa=false;
                } else {
                    return new LatLong(la, d);
                }

            } catch (NumberFormatException e){
                AppLog.d(src, p);
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
        return "Coordinates:\nLatitude:" +
                src.getLatitude() +
                "Longitude:" +
                src.getLongitude();
    }
}
