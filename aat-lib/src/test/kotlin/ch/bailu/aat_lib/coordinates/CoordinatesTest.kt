package ch.bailu.aat_lib.coordinates

import ch.bailu.aat_lib.app.AppConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.annotation.Nonnull

class CoordinatesTest {

    @Test
    fun testLocationParser() {
        assertThrows<Exception> {
            LocationParser.latLongFromString("blah")
        }

        val r1 = LocationParser.latLongFromString("18T 612284 5040357")
        val r2 = LocationParser.latLongFromString("geo:45.5077,-73.5626")
        val r3 = LocationParser.latLongFromString("35J 606572 7101729")
        val r4 = LocationParser.latLongFromString( "    geo:-26.2,28.066667")
        val r5 = LocationParser.latLongFromString("9FFGWP7Q+C3H")

        assertEquals(45.5077, r1.latitude, 0.001)
        assertEquals(-73.562, r1.longitude, 0.001)

        assertEquals(45.5077, r2.latitude, 0.001)
        assertEquals(-73.5626, r2.longitude, 0.001)

        assertEquals(-26.2000, r3.latitude, 0.001)
        assertEquals(28.0666, r3.longitude, 0.001)

        assertEquals(-26.2, r4.latitude, 0.001)
        assertEquals(28.066667, r4.longitude, 0.001)

        assertEquals(59.9135, r5.latitude, 0.001)
        assertEquals(10.7377, r5.longitude, 0.001)

    }


    @Test
    fun testSexagesimal() {
        val la = 47.629994
        val lo = 8.727773

        val lalo = LatLongE6(la, lo)

        var sla = WGS84Coordinates.Sexagesimal(la)
        var slo = WGS84Coordinates.Sexagesimal(lo)

        assertEquals(lalo.getLatitudeE6().toFloat(), sla.toE6().toFloat(), 0f)
        assertEquals(lalo.getLongitudeE6().toFloat(), slo.toE6().toFloat(), 0f)

        sla = WGS84Coordinates.Sexagesimal(sla.degree, sla.minute, sla.second)
        slo = WGS84Coordinates.Sexagesimal(slo.degree, slo.minute, slo.second)

        assertEquals(lalo.getLatitudeE6().toFloat(), sla.toE6().toFloat(), 0f)
        assertEquals(lalo.getLongitudeE6().toFloat(), slo.toE6().toFloat(), 0f)
    }


    @Test
    fun testCH1903Coordinates2() {
        // φ = 46° 2' 38.87" λ = 8° 43' 49.79" h = 650.60 m

        val la = WGS84Coordinates.Sexagesimal(46, 2, 38.87).toE6()
        val lo = WGS84Coordinates.Sexagesimal(8, 43, 49.79).toE6()

        val c = CH1903Coordinates(LatLongE6(la, lo))

        assertEquals(700000, c.easting)
        assertEquals(100000, c.northing)

        assertEquals(la.toFloat(), c.toLatLongE6().getLatitudeE6().toFloat(), 5f)
        assertEquals(lo.toFloat(), c.toLatLongE6().getLongitudeE6().toFloat(), 5f)


        val sla = WGS84Coordinates.Sexagesimal(c.toLatLong().latitude)
        val slo = WGS84Coordinates.Sexagesimal(c.toLatLong().longitude)

        //           ⇒ λ = 8° 43' 49.80" φ = 46° 02' 38.86"
        // aus NAVREF: λ = 8° 43' 49.79" φ = 46° 02' 38.87"    h = 650.60 m=
        assertEquals(8, slo.degree)
        assertEquals(43, slo.minute)
        assertEquals(49.80, slo.second, 0.005)

        assertEquals(46, sla.degree)
        assertEquals(2, sla.minute)
        assertEquals(38.86, sla.second, 0.005)
    }

    @Test
    fun testRorschach() {
        testCH1903Coordinate(754900, 260700, 47.478814, 9.493842)
        testUTMCoordinate(537208, 5258493, 32, 'T', 47.478814, 9.493842)
    }

    @Test
    fun testSantiagoChile() {
        testUTMCoordinate("19H 345093 6297582", "geo:-33.45,-70.666667")
    }

    private fun testUTMCoordinate(
        easting: Int,
        northing: Int,
        ezone: Int,
        nzone: Char,
        la: Double,
        lo: Double
    ) {
        val u1 = UTMCoordinates(la, lo)
        val u2 = UTMCoordinates(easting, northing, ezone, nzone)

        assertEquals(easting, u1.easting)
        assertEquals(northing, u1.northing)

        assertEquals(la, u1.toLatLong().latitude, 0.0001)
        assertEquals(lo, u1.toLatLong().longitude, 0.0001)

        assertEquals(nzone, u1.northingZone)
        assertEquals(ezone, u1.eastingZone)

        assertEquals(la < 0, u1.isInSouthernHemisphere)

        assertEquals(easting, u2.easting)
        assertEquals(northing, u2.northing)

        assertEquals(la, u2.toLatLong().latitude, 0.0001)
        assertEquals(lo, u2.toLatLong().longitude, 0.0001)

        assertEquals(nzone, u2.northingZone)
        assertEquals(ezone, u2.eastingZone)

        assertEquals(la < 0, u2.isInSouthernHemisphere)
    }

    @Test
    fun testGeneve() {
        testCH1903Coordinate(500532, 117325, 46.200013, 6.149985)
    }

    @Test
    fun testBern() {
        testCH1903Coordinate(600000, 200000, 46.951082786150465, 7.4386324175389165)
        testUTMCoordinate(381851, 5200553, 32, 'T', 46.94798, 7.44743)
    }

    @Test
    fun testBerlin() {
        testUTMCoordinate(392002, 5819913, 33, 'U', "geo:52.518611,13.408333")
    }

    private fun testUTMCoordinate(
        easting: Int,
        northing: Int,
        ezone: Int,
        nzone: Char,
        geoUrl: String
    ) {
        testUTMCoordinate(
            easting,
            northing,
            ezone,
            nzone,
            WGS84Coordinates(geoUrl).latitude.decimal,
            WGS84Coordinates(geoUrl).longitude.decimal
        )
    }

    @Test
    fun testMontreal() {
        testUTMCoordinate("18T 612284 5040357", "geo:45.5077,-73.5626")
    }


    @Test
    fun testJohannesburg() {
        testUTMCoordinate("35J 606572 7101729", "geo:-26.2,28.066667")
    }


    @Test
    fun testTokyo() {
        testUTMCoordinate("54S 381670 3950323", "geo:35.689722,139.692222")
    }

    @Test
    fun testJambi() {
        testUTMCoordinate("48N 345372 175795", "geo:1.59,103.61")
    }


    private fun testUTMCoordinate(utm: String, geoUrl: String) {
        val u = UTMCoordinates(utm)

        testUTMCoordinate(u.easting, u.northing, u.eastingZone, u.northingZone, geoUrl)
    }


    private fun testCH1903Coordinate(easting: Int, northing: Int, la: Double, lo: Double) {
        val latLongE6 = LatLongE6(la, lo)
        val laE6 = latLongE6.getLatitudeE6()
        val loE6 = latLongE6.getLongitudeE6()


        val l = LatLongE6(laE6, loE6)
        val c1 = CH1903Coordinates(l)
        val c2 = CH1903Coordinates(easting, northing)
        val c3 = CH1903Coordinates(la, lo)

        assertEquals(
            loE6.toFloat(),
            c1.toLatLongE6().getLongitudeE6().toFloat(),
            CH1903Coordinates.LO_PRECISION.toE6().toFloat()
        )
        assertEquals(
            laE6.toFloat(),
            c1.toLatLongE6().getLatitudeE6().toFloat(),
            CH1903Coordinates.LA_PRECISION.toE6().toFloat()
        )

        assertEquals(la, c1.toLatLong().latitude, CH1903Coordinates.LA_PRECISION.decimal)
        assertEquals(
            lo,
            c1.toLatLong().longitude,
            CH1903Coordinates.LO_PRECISION.decimal
        )

        assertEquals(
            loE6.toFloat(),
            c2.toLatLongE6().getLongitudeE6().toFloat(),
            CH1903Coordinates.LO_PRECISION.toE6().toFloat()
        )
        assertEquals(
            laE6.toFloat(),
            c2.toLatLongE6().getLatitudeE6().toFloat(),
            CH1903Coordinates.LA_PRECISION.toE6().toFloat()
        )

        assertEquals(la, c2.toLatLong().latitude, CH1903Coordinates.LA_PRECISION.decimal)
        assertEquals(
            lo,
            c2.toLatLong().longitude,
            CH1903Coordinates.LO_PRECISION.decimal
        )

        assertEquals(
            loE6.toFloat(),
            c3.toLatLongE6().getLongitudeE6().toFloat(),
            CH1903Coordinates.LO_PRECISION.toE6().toFloat()
        )
        assertEquals(
            laE6.toFloat(),
            c3.toLatLongE6().getLatitudeE6().toFloat(),
            CH1903Coordinates.LA_PRECISION.toE6().toFloat()
        )

        assertEquals(la, c3.toLatLong().latitude, 0.0001)
        assertEquals(lo, c3.toLatLong().longitude, 0.0001)


        assertEquals(c1.northing, c2.northing)
        assertEquals(c1.easting, c2.easting)
        assertEquals(c3.easting, c2.easting)
        assertEquals(c3.northing, c2.northing)

        assertEquals(northing, c1.northing)
        assertEquals(easting, c1.easting)

        assertEquals(northing, c2.northing)
        assertEquals(easting, c2.easting)

        assertEquals(northing, c3.northing)
        assertEquals(easting, c3.easting)

        assertEquals(c1.toLatLong().latitude, c2.toLatLong().latitude, 0.0001)
        assertEquals(c1.toLatLong().longitude, c2.toLatLong().longitude, 0.0001)
    }

    @Test
    fun testDem3() {
        var coord = Dem3Coordinates(0, 0)
        assertEquals(0, coord.getLatitudeE6())
        assertEquals(0, coord.getLongitudeE6())
        assertEquals("N00E000", coord.toString())

        coord = Dem3Coordinates(-0.2, 0.2)
        assertEquals(-1000000, coord.getLatitudeE6())
        assertEquals(0, coord.getLongitudeE6())
        assertEquals("S01E000", coord.toString())


        coord = Dem3Coordinates(-0.2, -0.2)
        assertEquals(-1000000, coord.getLatitudeE6())
        assertEquals(-1000000, coord.getLongitudeE6())
        assertEquals("S01W001", coord.toString())
    }

    companion object {
        private var initialized = false

        @JvmStatic
        @BeforeAll
        fun init() {
            if (!initialized) {
                AppConfig.setInstance(object : AppConfig() {
                    @get:Nonnull
                    override val appId: String
                        get() = ""

                    @get:Nonnull
                    override val appVersionName: String
                        get() = ""

                    override val appVersionCode: Int
                        get() = 0

                    override val isRelease: Boolean
                        get() = false
                })
                initialized = true
            }
        }
    }
}
