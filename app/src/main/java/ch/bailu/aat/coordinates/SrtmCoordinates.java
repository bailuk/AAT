package ch.bailu.aat.coordinates;

import android.content.Context;

import org.mapsforge.core.model.LatLong;

import java.io.File;
import java.util.Locale;

import ch.bailu.aat.preferences.SolidDataDirectory;

public class SrtmCoordinates extends Coordinates implements LatLongE6Interface {
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
    
    
    private final int la, lo;
    private final String string;
    
    
    public SrtmCoordinates(int la, int lo) {
        this(la/1e6,lo/1e6);
    }


    public SrtmCoordinates(double la, double lo) {
        this.la=(int)Math.floor(la);
        this.lo=(int)Math.floor(lo);
        string = toLaString() + toLoString();
    }

    
    
    public SrtmCoordinates(LatLongE6Interface p) {
        this(p.getLatitudeE6(), p.getLongitudeE6());
    }

    public SrtmCoordinates(LatLong p) {
        this(p.getLatitude(), p.getLongitude());
    }


    public String toLaString() {
        return String.format((Locale)null,"%c%02d", 
                WGS84Sexagesimal.getLatitudeChar(la),  Math.abs(la));
    }
    
    public String toLoString() {
        return String.format((Locale)null,"%c%03d",
                WGS84Sexagesimal.getLongitudeChar(lo), Math.abs(lo));

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
        return toFile(new SolidDataDirectory(context).getValueAsFile());
    }

    
    
    // obsolete location
    private File toSRTMFile(File base) {
        return new File(base, "/SRTM/" + toString() + ".SRTMGL3.hgt.zip");
    }


    @Override
    public int getLatitudeE6() {
        return la * (int)1e6;
    }


    @Override
    public int getLongitudeE6() {
        return lo * (int)1e6;
    }
}
