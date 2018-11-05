package ch.bailu.aat.coordinates;

import java.text.DecimalFormat;

public class WGS84Sexagesimal extends Coordinates {
    
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

        public int toDecimalDegree() {
            return (int) (coordinate * 1e6);
        }
        

        
        public int getDegree() {return deg;}
        public int getMinute() {return min;}
        public int getSecond() {return sec;}

        private final static DecimalFormat fX = new DecimalFormat("#");
        private final static DecimalFormat f00 = new DecimalFormat("00");

        public String toString() {
            return fX.format(Math.abs(deg)) + "\u00B0 "
                    + f00.format(min) + "\u0027 "
                    + f00.format(sec) + "\u0027\u0027";
        }
    }

    
    private final Sexagesimal longitude;
    private final Sexagesimal latitude;
    
    
    public WGS84Sexagesimal(double la, double lo) {
        latitude=new Sexagesimal(la);
        longitude=new Sexagesimal(lo);
    }
    
    public Sexagesimal getLongitude() {
        return longitude;
    }
    
    public Sexagesimal getLatitude() {
        return latitude;
    }


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
    

}
