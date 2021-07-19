package ch.bailu.aat_lib.coordinates;

import org.mapsforge.core.model.LatLong;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.logger.AppLog;


public class CH1903Coordinates extends MeterCoordinates {
    /**
     *
     * formula taken from the Document "Naeherungsloesungen fuer die direkte Transformation
     * CH1903 <=> WGS84" by the Bundesamt fuer Landestopografie swisstopo (http://www.swisstopo.ch)
     *
     */

    public final static double SECONDS=3600d;

    private final static double BERNE_LA=169028.66d/SECONDS;
    private final static double BERNE_LO=26782.5d/SECONDS;

    // x corespondents to northing and latitude
    public final static int BERNE_SIY=600000;

    // y corespondents to easting and longitude
    public final static int BERNE_SIX=200000;


    public final static WGS84Coordinates.Sexagesimal LA_PRECISSION = new WGS84Coordinates.Sexagesimal(0,0,0.12);
    public final static WGS84Coordinates.Sexagesimal LO_PRECISSION = new WGS84Coordinates.Sexagesimal(0,0,0.08);

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



    public CH1903Coordinates(String code) {
        code = code.trim();

        String[] parts = code.split("[,/ ]");




        int n=0;
        int e=0;

        for (String p : parts) {
            try {
                final double d = Double.parseDouble(p.trim());
                final int i;

                if (d < 1000d) i = (int) (d*1000d);
                else i = (int) d;

                if (i > 100000 && i < 300000) n = i;
                else if (i > 400000 && i < 800000) e = i;


            } catch (Exception ex) {
                AppLog.d(this, code + ": " + p);
            }
        }

        if (n == 0 || e == 0) {
            throw getCodeNotValidException(code);
        }

        easting = e;
        northing = n;
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
        double x3=x2*x;
        double y2=y*y;
        double y3=y2*y;

        // latitude phi (φ) => x northing
        double la1 = 16.9023892d
                +  3.238272d  *x
                -  0.270978d  *y2
                -  0.002528d  *x2
                -  0.0447d    *y2 *x
                -  0.0140d    *x3;
        int la =  toE6Degree(la1);


        // longitude lambda (λ) => y easting
        double lo1 = 2.6779094d
                   + 4.728982d * y
                   + 0.791484d * y * x
                   + 0.1306d   * y * x2
                   - 0.0436d   * y3;
        int lo = toE6Degree(lo1);

        return new LatLongE6(la,lo);
    }


    private static final double TOE6DEGREE = (1e6d / 36d) *100d;
    private static int toE6Degree(double c) {
        double result = c * TOE6DEGREE;
        long int_result = Math.round(result);
        return (int) int_result;
    }


    private static double getRelativeY(int si) {
        double rel = si - BERNE_SIY;
        return rel / 1e6d;
    }

    private static double getRelativeX(int si) {
        double rel = si - BERNE_SIX;
        return rel / 1e6d;
    }



    @Nonnull
    @Override
    public String toString() {
        return FF.f().N3_3.format(((float)northing)/1000f) + "/"
                + FF.f().N3_3.format(((float)easting)/1000f);
    }




    private static final BoundingBoxE6 SWISS_AREA = new BoundingBoxE6(48300000,11200000,45600000,5000000);

    public static boolean inSwitzerland(LatLong point) {
        return SWISS_AREA.contains(point);
    }

}
