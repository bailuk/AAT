package ch.bailu.aat.coordinates;

import java.io.File;
import java.util.Locale;

import org.osmdroid.api.IGeoPoint;

import android.content.Context;
import ch.bailu.aat.preferences.SolidDataDirectory;

public class SrtmCoordinates extends Coordinates {

    
    private final double la, lo;
    
    public SrtmCoordinates(int la, int lo) {
        this.la = la/1e6;
        this.lo = lo/1e6;
    }


    public SrtmCoordinates(double la, double lo) {
        this.la=la;
        this.lo=lo;
    }
    
    public SrtmCoordinates(IGeoPoint p) {
        this(p.getLatitudeE6(), p.getLongitudeE6());
    }

    
    @Override
    public String toString() {
        return String.format((Locale)null,"%c%02d%c%03d", 
               WGS84Sexagesimal.getLatitudeChar(Math.floor(la)),  Math.abs((int)Math.floor(la)),   
               WGS84Sexagesimal.getLongitudeChar(Math.floor(lo)), Math.abs((int)Math.floor(lo)));
        
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    
    public String toURL() {
        if (isAreaCovered()) {
            return ("http://e4ftl01.cr.usgs.gov/SRTM/SRTMGL3.003/2000.02.11/" + toString() + ".SRTMGL3.hgt.zip");
        } 
        return null;

    }
    
    private boolean isAreaCovered() {
        final int lat = (int) Math.floor(la);
        return (lat > -56 && lat < 60); 

    }


    public File toFile(File base) {
        return new File(base, "/SRTM/" + toString() + ".SRTMGL3.hgt.zip");
    }
    
    public File toFile(Context context) {
        return toFile(new SolidDataDirectory(context).toFile());
    }
    
    
    /*
    public SrtmCoordinates toTileOffset() {
        final int laDeg = getLatitude().getDegree(); 
        final int loDeg = getLongitude().getDegree();
        
        return new SrtmCoordinates((double)laDeg, (double)loDeg);
        
    }
    */
}
