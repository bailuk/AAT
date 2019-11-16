package ch.bailu.aat.coordinates;

import android.support.annotation.NonNull;

import org.mapsforge.core.model.LatLong;

import java.text.DecimalFormat;

import ch.bailu.aat.util.ui.AppLog;

public class WGS84Coordinates extends Coordinates {


    /**
     * WGS84 Sexagesimal and decimal operations
     */


    public static class Sexagesimal {
        public final int deg, min, sec;
        public final double coordinate;

        public Sexagesimal(double c) {
            coordinate = c;
            deg = (int)c;

            c = (Math.abs(c)-Math.abs(deg))*60d;
            min = (int)c;

            c = (c-min)*60d;
            sec = (int)Math.round(c);
        }

        public int toE6() {
            return (int) (coordinate * 1e6);
        }



        public int getDegree() {return deg;}
        public int getMinute() {return min;}
        public int getSecond() {return sec;}

        private final static DecimalFormat fX = new DecimalFormat("#");
        private final static DecimalFormat f00 = new DecimalFormat("00");

        @NonNull
        public String toString() {
            return fX.format(Math.abs(deg)) + "\u00B0 "
                    + f00.format(min) + "\u0027 "
                    + f00.format(sec) + "\u0027\u0027";
        }
    }


    private final Sexagesimal longitude;
    private final Sexagesimal latitude;


    public WGS84Coordinates(LatLong point) {
        this(point.getLatitude(), point.getLongitude());
    }

    public WGS84Coordinates(double la, double lo) {
        latitude=new Sexagesimal(la);
        longitude=new Sexagesimal(lo);
    }


    public WGS84Coordinates(String code) {
        String[] parts = code.split("[:,?#]");

        boolean scanLa=true;
        boolean scanned=false;


        double la=0d;
        double lo=0d;

        for (String p : parts) {
            try  {
                final double d = Double.parseDouble(p.trim());
                if (scanLa) {
                    la = d;
                    scanLa=false;
                } else {
                    lo = d;
                    scanned = true;
                }

            } catch (NumberFormatException e){
                AppLog.d(this, code + ": " + p);
            }
        }

        if (scanned) {
            latitude = new Sexagesimal(la);
            longitude = new Sexagesimal(lo);
        } else {
            throw getCodeNotValidException(code);
        }

    }



    public Sexagesimal getLongitude() {
        return longitude;
    }

    public Sexagesimal getLatitude() {
        return latitude;
    }


    @NonNull
    @Override
    public String toString() {
        return latitude.toString() + " "
                + getLatitudeChar() + " "
                + longitude.toString() + " "
                + getLongitudeChar();
    }




    public static char getLatitudeChar(double la) {
        if (la<0) return 'S';
        else return 'N';

    }

    public char getLatitudeChar() {
        return getLatitudeChar(latitude.coordinate);
    }

    public static char getLongitudeChar(double lo) {
        if (lo<0) return 'W';
        else return 'E';
    }

    public char getLongitudeChar() {
        return getLongitudeChar(longitude.coordinate);
    }


    public LatLong toLatLong() {
        return new LatLong(latitude.coordinate, longitude.coordinate);
    }

    public String getGeoUri() {
        return getGeoUri(toLatLong());
    }


    public static String getGeoUri(LatLong src) {
        return  "geo:" +
                src.getLatitude() +
                ',' +
                src.getLongitude();
    }


    public static String getGeoPointDescription(LatLong src) {
        return "Coordinates:\nLatitude:" +
                src.getLatitude() +
                "Longitude:" +
                src.getLongitude();
    }

}
