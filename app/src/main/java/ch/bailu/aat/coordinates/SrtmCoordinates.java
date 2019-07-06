package ch.bailu.aat.coordinates;

import android.content.Context;
import android.support.annotation.NonNull;

import org.mapsforge.core.model.LatLong;

import java.text.DecimalFormat;

import ch.bailu.aat.preferences.system.SolidDataDirectory;
import ch.bailu.util_java.foc.Foc;

public class SrtmCoordinates extends Coordinates implements LatLongE6Interface {
    /**
     * # Dem3LoaderTask:
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


    private final static DecimalFormat f2 = new DecimalFormat("00");
    private final static DecimalFormat f3 = new DecimalFormat("000");

    public String toLaString() {
        return WGS84Sexagesimal.getLatitudeChar(la) + f2.format(Math.abs(la));
    }


    public String toLoString() {
        return WGS84Sexagesimal.getLongitudeChar(lo) + f3.format(Math.abs(lo));
    }
    
    @NonNull
    @Override
    public String toString() {
        return string;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof SrtmCoordinates &&
                string.equals(obj.toString());


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

    public Foc toFile(Foc base) {
        Foc old = toSRTMFile(base);
        if (old.exists()) return old;

        return base.descendant("dem3/" + toExtString() + ".hgt.zip");
    }

    
    public Foc toFile(Context context) {
        return toFile(new SolidDataDirectory(context).getValueAsFile());
    }

    

    // obsolete location
    private Foc toSRTMFile(Foc base) {
        return base.descendant("SRTM/" + toString() + ".SRTMGL3.hgt.zip");
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
