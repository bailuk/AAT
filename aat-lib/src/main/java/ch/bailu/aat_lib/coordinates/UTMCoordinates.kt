package ch.bailu.aat_lib.coordinates

import ch.bailu.aat_lib.description.FF
import ch.bailu.aat_lib.exception.IllegalCodeException
import org.mapsforge.core.model.LatLong
import javax.annotation.Nonnull
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class UTMCoordinates : MeterCoordinates {
    private object EastingZones {
        private const val WIDTH_DEG = 6.0
        fun getZone(lon: Double): Int {
            val lo  = (lon + 180.0) / WIDTH_DEG
            return lo.toInt() + 1
        }
    }

    private object NorthingZones {
        private const val WIDTH_DEG = 8.0
        private val zonesSouth = charArrayOf('M', 'L', 'K', 'J', 'H', 'G', 'F', 'E', 'D', 'C')
        private val zonesNorth = charArrayOf('N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X')
        fun getZone(la: Double): Char {
            return if (la < 0) {
                getZone(abs(la), zonesSouth)
            } else getZone(la, zonesNorth)
        }

        private fun getZone(la: Double, zones: CharArray): Char {
            var i = (la / WIDTH_DEG).toInt()
            if (i >= zones.size) {
                i = zones.size - 1
            }
            return zones[i]
        }

        fun isValid(nZone: Char): Boolean {
            return findZone(nZone, zonesNorth) || findZone(nZone, zonesSouth)
        }

        fun isInSouthernHemisphere(nZone: Char): Boolean {
            return findZone(nZone, zonesSouth)
        }

        private fun findZone(nZone: Char, zones: CharArray): Boolean {
            for (x in zones) {
                if (nZone == x) return true
            }
            return false
        }
    }

    /**
     *
     * @return character representation of northing zone by this coordinate
     */
    val northingZone: Char
    val eastingZone: Int
    private var eastingDouble: Double = 0.0
    private var northingDouble: Double = 0.0

    constructor(p: LatLong) : this(p.getLatitude(), p.getLongitude()) {}
    constructor(la: Double, lo: Double) {
        eastingZone = EastingZones.getZone(lo)
        northingZone = NorthingZones.getZone(la)
        toUTM(Math.toRadians(la), Math.toRadians(lo), eastingZone)
    }

    constructor(e: Int, n: Int, eZone: Int, nZone: Char) {
        northingDouble = n.toDouble()
        eastingDouble = e.toDouble()
        eastingZone = eZone
        northingZone = nZone
    }


    /**
     * Parse string for a valid UTM coordinate to initialize this object
     * @param code string of the format "18T 612284 5040357"
     * (easting zone as number, northing zone as letter, easting in meters, northing in meters)
     *
     * @throws IllegalArgumentException if string can't be parsed
     */
    constructor(code: String) {
        val parts = code.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (parts.size != 3 || parts[0].length < 2 || parts[1].length < 6 || parts[2].length < 6) {
            throw IllegalCodeException(code)
        }
        try {
            northingZone = parts[0][parts[0].length - 1]
            if (NorthingZones.isValid(northingZone)) {
                eastingZone = Integer.valueOf(parts[0].substring(0, parts[0].length - 1))
                eastingDouble = Integer.valueOf(parts[1]).toDouble()
                northingDouble = Integer.valueOf(parts[2]).toDouble()
            } else {
                throw IllegalCodeException(code)
            }
        } catch (e: NumberFormatException) {
            throw IllegalCodeException(code)
        }
    }

    override fun round(dec: Int) {
        eastingDouble = round(eastingDouble.toInt(), dec).toDouble()
        northingDouble = round(northingDouble.toInt(), dec).toDouble()
    }

    val isInSouthernHemisphere: Boolean
        get() = NorthingZones.isInSouthernHemisphere(northingZone)

    override val northing: Int
        get() =  northingDouble.roundToInt()

    override val easting: Int
        get() =  eastingDouble.roundToInt()

    override fun toLatLong(): LatLong {
        return toLatLong(eastingDouble, northingDouble, eastingZone, isInSouthernHemisphere)
    }

    @Nonnull
    override fun toString(): String {
        return ("Z " + eastingZone + northingZone
                + ", E " + FF.f().N3_3.format((eastingDouble.toFloat() / 1000f).toDouble())
                + ", N " + FF.f().N3_3.format((northingDouble.toFloat() / 1000f).toDouble()))
    }

    private fun arcLengthOfMeridian(la: Double): Double {
        return M_ALPHA * (la + M_BETA * sin(2.0 * la) + M_GAMMA * sin(
            4.0 * la
        ) + M_DELTA * sin(6.0 * la) + M_EPSILON * sin(8.0 * la))
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
     * phi - Latitude of the point, in radians.
     * lambda - Longitude of the point, in radians.
     * lambda0 - Longitude of the central meridian to be used, in radians.
     *
     * Outputs:
     * xy - A 2-element array containing the x and y coordinates
     * of the computed point.
     *
     */
    private fun mapLatLonToXY(la: Double, lo: Double, lambda0: Double) {

        val nu2: Double
        val ep2: Double = (EQUATOR_RADIUS_M.pow(2.0) - POLAR_RADIUS_M.pow(2.0)) / POLAR_RADIUS_M.pow(
            2.0
        )
        nu2 = ep2 * cos(la).pow(2.0)
        val n: Double = EQUATOR_RADIUS_M.pow(2.0) / (POLAR_RADIUS_M * sqrt(1 + nu2))
        val t: Double = tan(la)
        val t2: Double = t * t
        val l: Double = lo - lambda0

        /* Precalculate coefficients for l**n in the equations below
           so a normal human being can read the expressions for easting
           and northing
           -- l**1 and l**2 have coefficients of 1.0 */
        val l3coef: Double = 1.0 - t2 + nu2
        val l4coef: Double = 5.0 - t2 + 9 * nu2 + 4.0 * (nu2 * nu2)
        val l5coef: Double = (5.0 - 18.0 * t2 + t2 * t2 + 14.0 * nu2
                - 58.0 * t2 * nu2)
        val l6coef: Double = (61.0 - 58.0 * t2 + t2 * t2 + 270.0 * nu2
                - 330.0 * t2 * nu2)
        val l7coef: Double = 61.0 - 479.0 * t2 + 179.0 * (t2 * t2) - t2 * t2 * t2
        val l8coef: Double = 1385.0 - 3111.0 * t2 + 543.0 * (t2 * t2) - t2 * t2 * t2

        /* Calculate easting (x) */eastingDouble =
            n * cos(la) * l + n / 6.0 * cos(la).pow(3.0) * l3coef * l.pow(3.0) + n / 120.0 * Math.pow(
                cos(la),
                5.0
            ) * l5coef * l.pow(5.0) + n / 5040.0 * cos(la).pow(7.0) * l7coef * l.pow(7.0)

        /* Calculate northing (y) */northingDouble =
            arcLengthOfMeridian(la) + t / 2.0 * n * cos(la).pow(2.0) * l.pow(2.0) + t / 24.0 * n * cos(
                la
            ).pow(4.0) * l4coef * l.pow(4.0) + t / 720.0 * n * cos(la).pow(6.0) * l6coef * l.pow(6.0) + t / 40320.0 * n * cos(la).pow(8.0) * l8coef * l.pow(8.0)
    }

    /**
     * LatLonToUTMXY
     *
     * Converts a latitude/longitude pair to x and y coordinates in the
     * Universal Transverse Mercator projection.
     *
     * Inputs:
     * lat - Latitude of the point, in radians.
     * lon - Longitude of the point, in radians.
     * zone - UTM zone to be used for calculating values for x and y.
     * If zone is less than 1 or greater than 60, the routine
     * will determine the appropriate zone from the value of lon.
     *
     * Outputs:
     * xy - A 2-element array where the UTM x and y values will be stored.
     *
     * Returns:
     * The UTM zone used for calculating the values of x and y.
     *
     */
    private fun toUTM(lat: Double, lon: Double, zone: Int) {
        mapLatLonToXY(lat, lon, getUTMCentralMeridian(zone.toDouble()))

        /* Adjust easting and northing for UTM system. */
        eastingDouble = eastingDouble * UTM_SCALE_FACTOR + 500000.0
        northingDouble *= UTM_SCALE_FACTOR
        if (northingDouble < 0.0) northingDouble += 10000000.0
    }

    companion object {
        /* Ellipsoid model constants (actual values here are for WGS84) */
        private const val EQUATOR_RADIUS_M = 6378137.0
        private const val POLAR_RADIUS_M = 6356752.314
        private const val UTM_SCALE_FACTOR = 0.9996
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
         * The ellipsoidal distance of the point from the equator, in meters.
         *
         */
        private const val N =
            (EQUATOR_RADIUS_M - POLAR_RADIUS_M) / (EQUATOR_RADIUS_M + POLAR_RADIUS_M)
        private val M_ALPHA = ((EQUATOR_RADIUS_M + POLAR_RADIUS_M) / 2.0
                * (1.0 + N.pow(2.0) / 4.0 + N.pow(4.0) / 64.0))
        private val M_BETA = -3.0 * N / 2.0 + 9.0 * N.pow(3.0) / 16.0 + -3.0 * N.pow(5.0) / 32.0
        private val M_GAMMA = 15.0 * N.pow(2.0) / 16.0 + -15.0 * N.pow(4.0) / 32.0
        private val M_DELTA = -35.0 * N.pow(3.0) / 48.0 + 105.0 * N.pow(5.0) / 256.0
        private val M_EPSILON = 315.0 * N.pow(4.0) / 512.0

        /**
         * getUTMCentralMeridian
         *
         * Determines the central meridian for the given UTM zone.
         *
         * Inputs:
         * zone - An integer value designating the UTM zone, range [1,60].
         *
         * Returns:
         * The central meridian for the given UTM zone, in radians, or zero
         * if the UTM zone parameter is outside the range [1,60].
         * Range of the central meridian is the radian equivalent of [-177,+177].
         *
         */
        private fun getUTMCentralMeridian(zone: Double): Double {
            return Math.toRadians(-183.0 + zone * 6.0)
        }

        /**
         * getFootpointLatitude
         *
         * Computes the footpoint latitude for use in converting transverse
         * Mercator coordinates to ellipsoidal coordinates.
         *
         * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
         * GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
         *
         * Inputs:
         * The UTM northing coordinate, in meters.
         *
         * Returns:
         * The footpoint latitude, in radians.
         *
         */
        private val F_ALPHA = ((EQUATOR_RADIUS_M + POLAR_RADIUS_M) / 2.0
                * (1 + N.pow(2.0) / 4 + N.pow(4.0) / 64))
        private val F_BETA = 3.0 * N / 2.0 + -27.0 * N.pow(3.0) / 32.0 + 269.0 * N.pow(5.0) / 512.0
        private val F_GAMMA = 21.0 * N.pow(2.0) / 16.0 + -55.0 * N.pow(4.0) / 32.0
        private val F_DELTA = 151.0 * N.pow(3.0) / 96.0 + -417.0 * N.pow(5.0) / 128.0
        private val F_EPSILON = 1097.0 * N.pow(4.0) / 512.0
        private fun getFootpointLatitude(n: Double): Double {
            val n1 = n / F_ALPHA
            return n1 + F_BETA * sin(2.0 * n1) + F_GAMMA * sin(
                4.0 * n1
            ) + F_DELTA * sin(6.0 * n1) + F_EPSILON * sin(8.0 * n1)
        }

        /**
         * mapXYToLatLon
         *
         * Converts x and y coordinates in the Transverse Mercator projection to
         * a latitude/longitude pair.  Note that Transverse Mercator is not
         * the same as UTM; a scale factor is required to convert between them.
         *
         * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
         * GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
         *
         * Inputs:
         * x - The easting of the point, in meters.
         * y - The northing of the point, in meters.
         * lambda0 - Longitude of the central meridian to be used, in radians.
         *
         * Outputs:
         * philambda - A 2-element containing the latitude and longitude
         * in radians.
         *
         * Remarks:
         * The local variables Nf, nuf2, tf, and tf2 serve the same purpose as
         * N, nu2, t, and t2 in MapLatLonToXY, but they are computed with respect
         * to the footpoint latitude phif.
         *
         * x1frac, x2frac, x2poly, x3poly, etc. are to enhance readability and
         * to optimize computations.
         *
         */
        private fun mapXYToLatLon(e: Double, n: Double, lambda0: Double): LatLong {
            val nf: Double
            val nuf2: Double
            val tf: Double
            val tf4: Double
            val cf: Double
            val x2Fractional: Double
            val x3Fractional: Double
            val x4Fractional: Double
            val x5Fractional: Double
            val x6Fractional: Double
            val x7Fractional: Double
            val x6poly: Double
            val x7poly: Double
            val phif: Double = getFootpointLatitude(n)
            val ep2: Double = ((EQUATOR_RADIUS_M.pow(2.0) - POLAR_RADIUS_M.pow(2.0))
                    / POLAR_RADIUS_M.pow(2.0))
            cf = cos(phif)
            nuf2 = ep2 * cf.pow(2.0)
            nf = EQUATOR_RADIUS_M.pow(2.0) / (POLAR_RADIUS_M * sqrt(1 + nuf2))
            var nfPow: Double = nf
            tf = tan(phif)
            val tf2: Double = tf * tf
            tf4 = tf2 * tf2

            /* Precalculate fractional coefficients for x**n in the equations
           below to simplify the expressions for latitude and longitude. */
            val x1Fractional: Double = 1.0 / (nfPow * cf)
            nfPow *= nf /* now equals Nf**2) */
            x2Fractional = tf / (2.0 * nfPow)
            nfPow *= nf /* now equals Nf**3) */
            x3Fractional = 1.0 / (6.0 * nfPow * cf)
            nfPow *= nf /* now equals Nf**4) */
            x4Fractional = tf / (24.0 * nfPow)
            nfPow *= nf /* now equals Nf**5) */
            x5Fractional = 1.0 / (120.0 * nfPow * cf)
            nfPow *= nf /* now equals Nf**6) */
            x6Fractional = tf / (720.0 * nfPow)
            nfPow *= nf /* now equals Nf**7) */
            x7Fractional = 1.0 / (5040.0 * nfPow * cf)
            nfPow *= nf /* now equals Nf**8) */
            val x8Fractional: Double = tf / (40320.0 * nfPow)

            /* Precalculate polynomial coefficients for x**n.
           -- x**1 does not have a polynomial coefficient. */
            val x2poly: Double = -1.0 - nuf2
            val x3poly: Double = -1.0 - 2 * tf2 - nuf2
            val x4poly: Double = 5.0 + 3.0 * tf2 + 6.0 * nuf2 - 6.0 * tf2 * nuf2 - 3.0 * (nuf2 * nuf2) - 9.0 * tf2 * (nuf2 * nuf2)
            val x5poly: Double = 5.0 + 28.0 * tf2 + 24.0 * tf4 + 6.0 * nuf2 + 8.0 * tf2 * nuf2
            x6poly = (-61.0 - 90.0 * tf2 - 45.0 * tf4 - 107.0 * nuf2
                    + 162.0 * tf2 * nuf2)
            x7poly = -61.0 - 662.0 * tf2 - 1320.0 * tf4 - 720.0 * (tf4 * tf2)
            val x8poly: Double = 1385.0 + 3633.0 * tf2 + 4095.0 * tf4 + 1575 * (tf4 * tf2)


            /* Calculate latitude */return LatLong(
                Math.toDegrees(
                    phif + x2Fractional * x2poly * (e * e) + x4Fractional * x4poly * e.pow(4.0) + x6Fractional * x6poly * e.pow(6.0) + x8Fractional * x8poly * e.pow(8.0)
                ),  /* Calculate longitude */
                Math.toDegrees(
                    lambda0 + x1Fractional * e + x3Fractional * x3poly * e.pow(3.0) + x5Fractional * x5poly * e.pow(5.0) + x7Fractional * x7poly * e.pow(7.0)
                )
            )
        }

        /**
         * UTMXYToLatLon
         *
         * Converts x and y coordinates in the Universal Transverse Mercator
         * projection to a latitude/longitude pair.
         *
         */
        private fun toLatLong(
            eastingIn: Double,
            northingIn: Double,
            zone: Int,
            southhemi: Boolean
        ): LatLong {
            var easting = eastingIn
            var northing = northingIn

            easting -= 500000.0
            easting /= UTM_SCALE_FACTOR

            /* If in southern hemisphere, adjust y accordingly. */
            if (southhemi) {
                northing -= 10000000.0
            }
            northing /= UTM_SCALE_FACTOR
            val cMeridian: Double = getUTMCentralMeridian(zone.toDouble())
            return mapXYToLatLon(easting, northing, cMeridian)
        }
    }
}
