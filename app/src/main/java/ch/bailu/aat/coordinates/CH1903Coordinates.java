package ch.bailu.aat.coordinates;

import org.mapsforge.core.model.LatLong;

import java.text.DecimalFormat;
import java.util.Locale;

import ch.bailu.aat.description.FF;


public class CH1903Coordinates extends MeterCoordinates {
    /**
     *  
     * formula taken from the Document "Naeherungsloesungen fuer die direkte Transformation
     * CH1903 <=> WGS84" by the Bundesamt fuer Landestopografie swisstopo (http://www.swisstopo.ch)
     * 
     */
    
    public final static double SECONDS=3600d;
    public final static double BERNE_LA=169028.66d/SECONDS;
    public final static double BERNE_LO=26782.5d/SECONDS;
    public final static int BERNE_SIX=200000;
    public final static int BERNE_SIY=600000;
    
    private int easting, northing;


    @Override
    public int getEasting() {
        return easting;
    }


    @Override
    public int getNorthing() {
        return northing;
    }


    public CH1903Coordinates(int e, int n) {
        easting=e;
        northing=n;
    }


    public CH1903Coordinates(double la, double lo) {
        toCH1903(la, lo);
    }

    public CH1903Coordinates(LatLong p) {
        this(p.getLatitude(), p.getLongitude());
    }

    public CH1903Coordinates(LatLongE6Interface point) {
        toCH1903(((double)point.getLatitudeE6())/1e6d, 
        		((double)point.getLongitudeE6())/1e6d);
    }


    private void toCH1903(double la, double lo) {
        la = getRelativeLatitude(la);
        lo = getRelativeLongitude(lo);

        double la2=la*la;
        double la3=la2*la;
        double lo2=lo*lo;
        double lo3=lo2*lo;
        
        northing = (int) Math.round 
        (
              200147.07d
            + 308807.95d *       la
            + 3745.25d   * lo2
            + 76.63d     *       la2
            - 194.56d    * lo2 * la 
            + 119.79d    *       la3
        );
    
        easting = (int) Math.round
        (
              600072.37d 
            + 211455.93d * lo
            - 10938.51d  * lo * la
            - 0.36d      * lo * la2
            - 44.54d     * lo3
         );
    }


    
    private static double getRelativeLatitude(double la) {
        return ((la - BERNE_LA) *SECONDS) / 10000d;
    }
    
    private static double getRelativeLongitude(double lo) {
        return ((lo - BERNE_LO) *SECONDS) / 10000d;
    }

    
    public void round(int c) {
        easting=round(easting,c);
        northing=round(northing,c);
    }


    @Override
    public LatLong toLatLong() {
        return toLatLongE6().toLatLong();

    }

    public LatLongE6 toLatLongE6() {
        double x = getRelativeX(northing);
        double y = getRelativeY(easting);
            
        double x2=x*x;
        double y3=y*y*y;
        double x3=x2*x;
        double y2=y*y;
            
        return new LatLongE6(
                toDecimalDegree(
                  16.9023892d
                +  3.238272d     *x
                -  0.270978d *y2
                -  0.002528d     *x2
                -  0.0447d   *y2 *x 
                -  0.0140d       *x3),
                
                toDecimalDegree(
                  2.6779094d
                + 4.728982d * y
                + 0.791484d * y * x
                + 0.1306d   * y * x2
                - 0.0436d   * y3)
        );
    }


    private static double toDecimalDegree(double c) {
        return c * 100d / 36d;
    }
    
    private static double getRelativeY(int si) {
        return ( (double)(si - BERNE_SIY) ) / 1e6d;
    }

    private static double getRelativeX(int si) {
        return ( (double)(si - BERNE_SIX) ) / 1e6d;
    }
    


    @Override
    public String toString() {
        return FF.N3_3.format(((float)northing)/1000f) + "/"
                + FF.N3_3.format(((float)easting)/1000f);
    }
    



    private static final BoundingBoxE6 SWISS_AREA = new BoundingBoxE6(48300000,11200000,45600000,5000000);

    public static boolean inSwitzerland(LatLong point) {
        return SWISS_AREA.contains(point);
    }

}
