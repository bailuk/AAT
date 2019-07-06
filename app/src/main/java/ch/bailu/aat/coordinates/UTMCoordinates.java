package ch.bailu.aat.coordinates;

import android.support.annotation.NonNull;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.description.FF;

public class UTMCoordinates extends MeterCoordinates {



    private static class EastingZones {
        private final static double WIDTH_DEG=6d;
        //private static final int MIDDLE_ZONE=31;
        
        public static int getZone(double lo) {
            lo = lo + 180d;
            lo /= WIDTH_DEG;
            
            int x=(int)lo;
            return x+1;
            
        }
    }

    
    private static class NorthingZones {
        private final static int MIDDLE_ZONE=10;
        private final static int WIDTH_DEG=8;
        
        private static final char[] zones = 
            new char[] {'C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X'};
        
        
        private static int getZone(double la) {
            int x = ((int)la)/WIDTH_DEG;
            return x+MIDDLE_ZONE;
        }
        
        public static char getZoneCharacter(int z) {
            if (z>=zones.length) z=zones.length-1;
            return zones[z];
        }
    }

    
    /* Ellipsoid model constants (actual values here are for WGS84) */
    private final static double EQUATOR_RADIUS_M = 6378137.0;
    private final static double POLAR_RADIUS_M = 6356752.314;

    private final static double UTM_SCALE_FACTOR = 0.9996;

    
    private int nzone;
    private final int ezone;
    double easting, northing;
    private final boolean south;

    public UTMCoordinates(LatLong p) {
        this(p.getLatitude(), p.getLongitude());
    }

    public UTMCoordinates(double la, double lo) {
        ezone=EastingZones.getZone(lo);
        nzone=NorthingZones.getZone(la);
        south=(la<0d);
        
        toUTM(Math.toRadians(la), Math.toRadians(lo), ezone);
    }

    public UTMCoordinates(int e, int n, int z, boolean s) {
        northing=n;
        easting=e;
        ezone=z;
        south=s;
    }
    
    
    public UTMCoordinates(LatLongE6Interface point) {
        this(((double)point.getLatitudeE6())/1e6d, ((double)point.getLongitudeE6())/1e6d);
    }

    
    public void round(int c) {
        easting=round((int)easting,c);
        northing=round((int)northing,c);
    }
    
    public int getZone() {
        return ezone;
    }
    
    public int getNorthingZone() {
        return nzone;
    }
    
    public boolean isInSouthernHemnisphere() {
        return south;
    }
    
    public char getNorthingZoneCharacter() {
        return NorthingZones.getZoneCharacter(nzone);
    }

    @Override
    public int getNorthing() {
        return (int)Math.round(northing);
    }

    @Override
    public int getEasting() {
        return (int)Math.round(easting);
    }
    
    
    public LatLongE6 toLatLongE6() {
        return new LatLongE6(toLatLong());
    }

    @Override
    public LatLong toLatLong() {
        return toLatLongE6(easting, northing, ezone, south);

    }

    private final FF f = FF.f();

    @NonNull
    @Override
    public String toString() {

        return "Z " + ezone + getNorthingZoneCharacter()
                + ", E " + f.N3_3.format(((float)easting)/1000f)
                + ", N " + f.N3_3.format(((float)northing)/1000f);
    }
    
    /*
     * 
     * UTM converting functions adapted from http://home.hiwaay.net/~taylorc/toolbox/geography/geoutm.html
     * Copyright 1997-1998 by Charles L. Taylor
     * 
     */
    
    /**
    * ArcLengthOfMeridian
    *
    * Computes the ellipsoidal distance from the equator to a point at a
    * given latitude.
    *
    * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
    * GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
    *
    * Returns:
    *     The ellipsoidal distance of the point from the equator, in meters.
    *
    */
    private final static double N = (EQUATOR_RADIUS_M - POLAR_RADIUS_M) / (EQUATOR_RADIUS_M + POLAR_RADIUS_M);
    
    private final static double M_ALPHA = ((EQUATOR_RADIUS_M + POLAR_RADIUS_M) / 2.0)
    * (1.0 + (Math.pow (N, 2.0) / 4.0) + (Math.pow (N, 4.0) / 64.0));

    private final static double M_BETA = (-3.0 * N / 2.0) + (9.0 * Math.pow (N, 3.0) / 16.0)
    + (-3.0 * Math.pow (N, 5.0) / 32.0);

    private final static double M_GAMMA = (15.0 * Math.pow (N, 2.0) / 16.0)
     + (-15.0 * Math.pow (N, 4.0) / 32.0);

    private final static double M_DELTA = (-35.0 * Math.pow (N, 3.0) / 48.0)
     + (105.0 * Math.pow (N, 5.0) / 256.0);

    private final static double M_EPSILON = (315.0 * Math.pow (N, 4.0) / 512.0);

    
    private double ArcLengthOfMeridian (double la)
    {
        return   M_ALPHA * (la 
              + (M_BETA * Math.sin (2.0 * la))
              + (M_GAMMA * Math.sin (4.0 * la))
              + (M_DELTA * Math.sin (6.0 * la))
              + (M_EPSILON * Math.sin (8.0 * la)));
    }



    /**
    * UTMCentralMeridian
    *
    * Determines the central meridian for the given UTM zone.
    *
    * Inputs:
    *     zone - An integer value designating the UTM zone, range [1,60].
    *
    * Returns:
    *   The central meridian for the given UTM zone, in radians, or zero
    *   if the UTM zone parameter is outside the range [1,60].
    *   Range of the central meridian is the radian equivalent of [-177,+177].
    *
    */
    private static double UTMCentralMeridian (double zone)
    {
        return Math.toRadians((-183.0 + (zone * 6.0)));
    }



    /**
    * FootpointLatitude
    *
    * Computes the footpoint latitude for use in converting transverse
    * Mercator coordinates to ellipsoidal coordinates.
    *
    * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
    *   GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
    *
    * Inputs:
    *   The UTM northing coordinate, in meters.
    *
    * Returns:
    *   The footpoint latitude, in radians.
    *
    */
    
    
    
    private final static double F_ALPHA = ((EQUATOR_RADIUS_M + POLAR_RADIUS_M) / 2.0)
    * (1 + (Math.pow (N, 2.0) / 4) + (Math.pow (N, 4.0) / 64));


    private final static double F_BETA = (3.0 * N / 2.0) + (-27.0 * Math.pow (N, 3.0) / 32.0)
    + (269.0 * Math.pow (N, 5.0) / 512.0);

    private final static double F_GAMMA = (21.0 * Math.pow (N, 2.0) / 16.0)
    + (-55.0 * Math.pow (N, 4.0) / 32.0);

    private final static double F_DELTA = (151.0 * Math.pow (N, 3.0) / 96.0)
    + (-417.0 * Math.pow (N, 5.0) / 128.0);

    private final static double F_EPSILON = (1097.0 * Math.pow (N, 4.0) / 512.0);
    
    private static double FootpointLatitude (double n)
    {
        double n1 = n / F_ALPHA;
        
        return n1 + (F_BETA * Math.sin (2.0 * n1))
            + (F_GAMMA * Math.sin (4.0 * n1))
            + (F_DELTA * Math.sin (6.0 * n1))
            + (F_EPSILON * Math.sin (8.0 * n1));
        
    }



    /**
    * MapLatLonToXY
    *
    * Converts a latitude/longitude pair to x and y coordinates in the
    * Transverse Mercator projection.  Note that Transverse Mercator is not
    * the same as UTM; a scale factor is required to convert between them.
    *
    * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
    * GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
    *
    * Inputs:
    *    phi - Latitude of the point, in radians.
    *    lambda - Longitude of the point, in radians.
    *    lambda0 - Longitude of the central meridian to be used, in radians.
    *
    * Outputs:
    *    xy - A 2-element array containing the x and y coordinates
    *         of the computed point.
    *
    */
    private void MapLatLonToXY (double la, double lo, double lambda0)
    {
        double N, nu2, ep2, t, t2, l;
        double l3coef, l4coef, l5coef, l6coef, l7coef, l8coef;

        ep2 = (Math.pow (EQUATOR_RADIUS_M, 2.0) - Math.pow (POLAR_RADIUS_M, 2.0)) / Math.pow (POLAR_RADIUS_M, 2.0);
        nu2 = ep2 * Math.pow (Math.cos (la), 2.0);

        N = Math.pow (EQUATOR_RADIUS_M, 2.0) / (POLAR_RADIUS_M * Math.sqrt (1 + nu2));
    
        t = Math.tan (la);
        t2 = t * t;

        l = lo - lambda0;
    
        /* Precalculate coefficients for l**n in the equations below
           so a normal human being can read the expressions for easting
           and northing
           -- l**1 and l**2 have coefficients of 1.0 */
        l3coef = 1.0 - t2 + nu2;
    
        l4coef = 5.0 - t2 + 9 * nu2 + 4.0 * (nu2 * nu2);
    
        l5coef = 5.0 - 18.0 * t2 + (t2 * t2) + 14.0 * nu2
            - 58.0 * t2 * nu2;
    
        l6coef = 61.0 - 58.0 * t2 + (t2 * t2) + 270.0 * nu2
            - 330.0 * t2 * nu2;
    
        l7coef = 61.0 - 479.0 * t2 + 179.0 * (t2 * t2) - (t2 * t2 * t2);
    
        l8coef = 1385.0 - 3111.0 * t2 + 543.0 * (t2 * t2) - (t2 * t2 * t2);
    
        /* Calculate easting (x) */
        easting = (N * Math.cos (la) * l
            + (N / 6.0 * Math.pow (Math.cos (la), 3.0) * l3coef * Math.pow (l, 3.0))
            + (N / 120.0 * Math.pow (Math.cos (la), 5.0) * l5coef * Math.pow (l, 5.0))
            + (N / 5040.0 * Math.pow (Math.cos (la), 7.0) * l7coef * Math.pow (l, 7.0)));
    
        /* Calculate northing (y) */
        northing = (ArcLengthOfMeridian (la)
            + (t / 2.0 * N * Math.pow (Math.cos (la), 2.0) * Math.pow (l, 2.0))
            + (t / 24.0 * N * Math.pow (Math.cos (la), 4.0) * l4coef * Math.pow (l, 4.0))
            + (t / 720.0 * N * Math.pow (Math.cos (la), 6.0) * l6coef * Math.pow (l, 6.0))
            + (t / 40320.0 * N * Math.pow (Math.cos (la), 8.0) * l8coef * Math.pow (l, 8.0)));
    
    }
    
    
    
    /**
    * MapXYToLatLon
    *
    * Converts x and y coordinates in the Transverse Mercator projection to
    * a latitude/longitude pair.  Note that Transverse Mercator is not
    * the same as UTM; a scale factor is required to convert between them.
    *
    * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
    *   GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
    *
    * Inputs:
    *   x - The easting of the point, in meters.
    *   y - The northing of the point, in meters.
    *   lambda0 - Longitude of the central meridian to be used, in radians.
    *
    * Outputs:
    *   philambda - A 2-element containing the latitude and longitude
    *               in radians.
    *
    * Remarks:
    *   The local variables Nf, nuf2, tf, and tf2 serve the same purpose as
    *   N, nu2, t, and t2 in MapLatLonToXY, but they are computed with respect
    *   to the footpoint latitude phif.
    *
    *   x1frac, x2frac, x2poly, x3poly, etc. are to enhance readability and
    *   to optimize computations.
    *
    */
    
    
    private static LatLong MapXYToLatLon (double e, double n, double lambda0)
    {
        double phif, Nf, Nfpow, nuf2, ep2, tf, tf2, tf4, cf;
        double x1frac, x2frac, x3frac, x4frac, x5frac, x6frac, x7frac, x8frac;
        double x2poly, x3poly, x4poly, x5poly, x6poly, x7poly, x8poly;
    
        phif = FootpointLatitude (n);
        
        ep2 = (Math.pow (EQUATOR_RADIUS_M, 2.0) - Math.pow (POLAR_RADIUS_M, 2.0))
              / Math.pow (POLAR_RADIUS_M, 2.0);
        
        cf = Math.cos (phif);
        
        nuf2 = ep2 * Math.pow (cf, 2.0);
        
        Nf = Math.pow (EQUATOR_RADIUS_M, 2.0) / (POLAR_RADIUS_M * Math.sqrt (1 + nuf2));
        Nfpow = Nf;
        
        tf = Math.tan (phif);
        tf2 = tf * tf;
        tf4 = tf2 * tf2;
        
        /* Precalculate fractional coefficients for x**n in the equations
           below to simplify the expressions for latitude and longitude. */
        x1frac = 1.0 / (Nfpow * cf);
        
        Nfpow *= Nf;   /* now equals Nf**2) */
        x2frac = tf / (2.0 * Nfpow);
        
        Nfpow *= Nf;   /* now equals Nf**3) */
        x3frac = 1.0 / (6.0 * Nfpow * cf);
        
        Nfpow *= Nf;   /* now equals Nf**4) */
        x4frac = tf / (24.0 * Nfpow);
        
        Nfpow *= Nf;   /* now equals Nf**5) */
        x5frac = 1.0 / (120.0 * Nfpow * cf);
        
        Nfpow *= Nf;   /* now equals Nf**6) */
        x6frac = tf / (720.0 * Nfpow);
        
        Nfpow *= Nf;   /* now equals Nf**7) */
        x7frac = 1.0 / (5040.0 * Nfpow * cf);
        
        Nfpow *= Nf;   /* now equals Nf**8) */
        x8frac = tf / (40320.0 * Nfpow);
        
        /* Precalculate polynomial coefficients for x**n.
           -- x**1 does not have a polynomial coefficient. */
        x2poly = -1.0 - nuf2;
        
        x3poly = -1.0 - 2 * tf2 - nuf2;
        
        x4poly = 5.0 + 3.0 * tf2 + 6.0 * nuf2 - 6.0 * tf2 * nuf2
        - 3.0 * (nuf2 *nuf2) - 9.0 * tf2 * (nuf2 * nuf2);
        
        x5poly = 5.0 + 28.0 * tf2 + 24.0 * tf4 + 6.0 * nuf2 + 8.0 * tf2 * nuf2;
        
        x6poly = -61.0 - 90.0 * tf2 - 45.0 * tf4 - 107.0 * nuf2
        + 162.0 * tf2 * nuf2;
        
        x7poly = -61.0 - 662.0 * tf2 - 1320.0 * tf4 - 720.0 * (tf4 * tf2);
        
        x8poly = 1385.0 + 3633.0 * tf2 + 4095.0 * tf4 + 1575 * (tf4 * tf2);
        
        
        /* Calculate latitude */
        
        return new LatLong( Math.toDegrees( phif + x2frac * x2poly * (e * e)
        + x4frac * x4poly * Math.pow (e, 4.0)
        + x6frac * x6poly * Math.pow (e, 6.0)
        + x8frac * x8poly * Math.pow (e, 8.0) ),
        
        /* Calculate longitude */
        Math.toDegrees( lambda0 + x1frac * e
        + x3frac * x3poly * Math.pow (e, 3.0)
        + x5frac * x5poly * Math.pow (e, 5.0)
        + x7frac * x7poly * Math.pow (e, 7.0)));
        
    }



    /**
    * LatLonToUTMXY
    *
    * Converts a latitude/longitude pair to x and y coordinates in the
    * Universal Transverse Mercator projection.
    *
    * Inputs:
    *   lat - Latitude of the point, in radians.
    *   lon - Longitude of the point, in radians.
    *   zone - UTM zone to be used for calculating values for x and y.
    *          If zone is less than 1 or greater than 60, the routine
    *          will determine the appropriate zone from the value of lon.
    *
    * Outputs:
    *   xy - A 2-element array where the UTM x and y values will be stored.
    *
    * Returns:
    *   The UTM zone used for calculating the values of x and y.
    *
    */
    private void toUTM (double lat, double lon, int zone)
    {
        MapLatLonToXY (lat, lon, UTMCentralMeridian(zone));

        /* Adjust easting and northing for UTM system. */
        easting = (easting * UTM_SCALE_FACTOR) + 500000.0d;
        northing = northing * UTM_SCALE_FACTOR;
        if (northing < 0d)
            northing = northing + 10000000d;

    }
    
    
    
    /**
    * UTMXYToLatLon
    *
    * Converts x and y coordinates in the Universal Transverse Mercator
    * projection to a latitude/longitude pair.
    *
    */
    
    private static LatLong toLatLongE6(double easting, double northing, int zone, boolean southhemi)
    {
        double cmeridian;
        
        easting -= 500000.0;
        easting /= UTM_SCALE_FACTOR;
        
        /* If in southern hemisphere, adjust y accordingly. */
        if (southhemi) {
            northing -= 10000000.0;
        }
        
        northing /= UTM_SCALE_FACTOR;
        
        cmeridian = UTMCentralMeridian (zone);
        return MapXYToLatLon(easting, northing, cmeridian);
    }

}
