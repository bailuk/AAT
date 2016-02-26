package ch.bailu.aat.coordinates;

import java.io.File;
import java.util.Locale;

import org.osmdroid.api.IGeoPoint;

import android.content.Context;
import ch.bailu.aat.preferences.SolidDataDirectory;

public class SrtmCoordinates extends Coordinates {
    /**
     * # Dem3:
     * Digital Elevation Models in 3 arc / second resolution. 
     * 
     * # Source:
     * All tiles are taken from http://viewfinderpanoramas.org/dem3.html and repackaged.
     * Most tiles are originally from the 2000 Shuttle Radar Topography Mission. 
     * See http://viewfinderpanoramas.org/dem3 for details.
     * 
     * # Obsolete download location:
     * private final static String base_url_srtm= "http://e4ftl01.cr.usgs.gov/SRTM/SRTMGL3.003/2000.02.11/";
     *  
     */
    private final static String base_url= "http://bailu.ch/dem3/";
    
    
    private double la, lo;
    private String string;
    
    
    public SrtmCoordinates(int la, int lo) {
        set(la,lo);
    }


    public SrtmCoordinates(double la, double lo) {
        set(la,lo);
    }

    
    public void set(int la, int lo) {
        set(la/1e6,lo/1e6);
    }
    
    
    public void set(double la, double lo) {
        this.la=la;
        this.lo=lo;
        string = toLaString() + toLoString();
    }

    
    public SrtmCoordinates(IGeoPoint p) {
        this(p.getLatitudeE6(), p.getLongitudeE6());
    }

    
    
    public String toLaString() {
        return String.format((Locale)null,"%c%02d", 
                WGS84Sexagesimal.getLatitudeChar(Math.floor(la)),  Math.abs((int)Math.floor(la)));
    }
    
    public String toLoString() {
        return String.format((Locale)null,"%c%03d",
                WGS84Sexagesimal.getLongitudeChar(Math.floor(lo)), Math.abs((int)Math.floor(lo)));

    }
    
    @Override
    public String toString() {
        return string;
    }

    
    @Override
    public int hashCode() {
        return string.hashCode();
    }
    
    public String toExtString() {
        return toLaString() + "/" + toString();
    }

    public String toURL() {
        return (base_url + toExtString() + ".hgt.zip");
    }

    
    
    public File toFile(File base) {
        File old = toSRTMFile(base);
        if (old.exists()) return old;
        
        return new File(base, "/dem3/"+ toExtString() + ".hgt.zip");
    }

    
    public File toFile(Context context) {
        return toFile(new SolidDataDirectory(context).toFile());
    }

    
    
    // obsolete location
    private File toSRTMFile(File base) {
        return new File(base, "/SRTM/" + toString() + ".SRTMGL3.hgt.zip");
    }

    /*
    private String toSRTMURL() {
        if (isAreaCovered()) {
            return (base_url_srtm + toString() + ".SRTMGL3.hgt.zip");
        } 
        return null;

    }

    private boolean isAreaCovered() {
        final int lat = (int) Math.floor(la);
        return (lat > -56 && lat < 60); 

    }
    */
    
    /*
    public SrtmCoordinates toTileOffset() {
        final int laDeg = getLatitude().getDegree(); 
        final int loDeg = getLongitude().getDegree();
        
        return new SrtmCoordinates((double)laDeg, (double)loDeg);
        
    }
    */
}
