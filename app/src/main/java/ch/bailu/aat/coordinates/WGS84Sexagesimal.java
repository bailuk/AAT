package ch.bailu.aat.coordinates;

import java.util.Locale;

import org.osmdroid.api.IGeoPoint;

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
            return (int) (coordinate * 1e6);//toDecimalDegree(deg,min,sec);
        }
        
        //public static  double toDecimalDegree(int d, int m, float s) {
        //    return d + ((double)m)/60d + ((double)s)/3600d;
        //}
        
        
        public int getDegree() {return deg;}
        public int getMinute() {return min;}
        public int getSecond() {return sec;}
        
        public String toString() {
            return String.format((Locale)null,"%2d\u00B0 %02d\u0027 %02d\u0027\u0027", Math.abs(deg), min, sec);
        }
    }

    
    private Sexagesimal longitude,latitude;
    
    
    public WGS84Sexagesimal(double la, double lo) {
        latitude=new Sexagesimal(la);
        longitude=new Sexagesimal(lo);
    }
    
    public WGS84Sexagesimal(IGeoPoint p) {
        this(((double)p.getLatitudeE6())/1e6d, ((double)p.getLongitudeE6())/1e6d);
    }

    public Sexagesimal getLongitude() {
        return longitude;
    }
    
    public Sexagesimal getLatitude() {
        return latitude;
    }
    
    @Override
    public String toString() {
        
        return String.format((Locale)null,"%s %c  %s %c", 
                latitude.toString(),  getLatitudeChar(), 
                longitude.toString(), getLongitudeChar());
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
