package ch.bailu.aat_lib.coordinates

import ch.bailu.aat_lib.coordinates.WGS84Coordinates.Sexagesimal
import ch.bailu.aat_lib.description.FF
import ch.bailu.aat_lib.logger.AppLog
import org.mapsforge.core.model.LatLong
import kotlin.math.roundToInt

class CH1903Coordinates : MeterCoordinates {
    override var easting = 0
        private set

    override var northing = 0
        private set

    constructor(e: Int, n: Int) {
        easting = e
        northing = n
    }

    constructor(code: String) {
        val parts = code.trim { it <= ' ' }.split("[,/ ]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var n = 0
        var e = 0
        for (p in parts) {
            try {
                val d = p.trim { it <= ' ' }.toDouble()
                val i: Int = if (d < 1000.0) (d * 1000.0).toInt() else d.toInt()
                if (i > 100000 && i < 300000) n = i else if (i > 400000 && i < 800000) e = i
            } catch (ex: Exception) {
                AppLog.d(this, "$code: $p")
            }
        }
        if (n == 0 || e == 0) {
            throw createIllegalCodeException(code)
        }
        easting = e
        northing = n
    }

    constructor(la: Double, lo: Double) {
        toCH1903(la, lo)
    }

    constructor(p: LatLong) : this(p.getLatitude(), p.getLongitude()) {}
    constructor(point: LatLongInterface) {
        toCH1903(
            point.getLatitudeE6().toDouble() / 1e6,
            point.getLongitudeE6().toDouble() / 1e6
        )
    }

    private fun toCH1903(lat: Double, lon: Double) {
        val la = getRelativeLatitude(lat)
        val lo = getRelativeLongitude(lon)
        val la2 = la * la
        val la3 = la2 * la
        val lo2 = lo * lo
        val lo3 = lo2 * lo
        northing = (200147.07 + 308807.95 * la + 3745.25 * lo2 + 76.63 * la2
                - 194.56 * lo2 * la
                + 119.79 * la3).roundToInt()
        easting = ((600072.37
                + 211455.93 * lo) - 10938.51 * lo * la - 0.36 * lo * la2 - 44.54 * lo3).roundToInt()
    }

    override fun round(dec: Int) {
        easting = round(easting, dec)
        northing = round(northing, dec)
    }

    override fun toLatLong(): LatLong {
        return toLatLongE6().toLatLong()
    }

    fun toLatLongE6(): LatLongE6 {
        val x = getRelativeX(northing)
        val y = getRelativeY(easting)
        val x2 = x * x
        val x3 = x2 * x
        val y2 = y * y
        val y3 = y2 * y

        // latitude phi (φ) => x northing
        val la1 = (16.9023892
                + 3.238272 * x) - 0.270978 * y2 - 0.002528 * x2 - 0.0447 * y2 * x - 0.0140 * x3
        val la = toE6Degree(la1)


        // longitude lambda (λ) => y easting
        val lo1 = (2.6779094 + 4.728982 * y + 0.791484 * y * x + 0.1306 * y * x2
                - 0.0436 * y3)
        val lo = toE6Degree(lo1)
        return LatLongE6(la, lo)
    }

    override fun toString(): String {
        return (FF.f().N3_3.format((northing.toFloat() / 1000f).toDouble()) + "/"
                + FF.f().N3_3.format((easting.toFloat() / 1000f).toDouble()))
    }

    companion object {
        /**
         *
         * formula taken from the Document "Naeherungsloesungen fuer die direkte Transformation
         * CH1903 <=> WGS84" by the Bundesamt fuer Landestopografie swisstopo (http://www.swisstopo.ch)
         *
         */
        private const val SECONDS = 3600.0
        private const val BERNE_LA = 169028.66 / SECONDS
        private const val BERNE_LO = 26782.5 / SECONDS

        // x corespondents to northing and latitude
        private const val BERNE_SIY = 600000

        // y corespondents to easting and longitude
        private const val BERNE_SIX = 200000

        @JvmField
        val LA_PRECISION = Sexagesimal(0, 0, 0.12)
        @JvmField
        val LO_PRECISION = Sexagesimal(0, 0, 0.08)
        private fun getRelativeLatitude(la: Double): Double {
            return (la - BERNE_LA) * SECONDS / 10000.0
        }

        private fun getRelativeLongitude(lo: Double): Double {
            return (lo - BERNE_LO) * SECONDS / 10000.0
        }

        private const val TOE6DEGREE = 1e6 / 36.0 * 100.0
        private fun toE6Degree(c: Double): Int {
            val result = c * TOE6DEGREE
            return result.roundToInt()
        }

        private fun getRelativeY(si: Int): Double {
            val rel = (si - BERNE_SIY).toDouble()
            return rel / 1e6
        }

        private fun getRelativeX(si: Int): Double {
            val rel = (si - BERNE_SIX).toDouble()
            return rel / 1e6
        }

        private val SWISS_AREA = BoundingBoxE6(48300000, 11200000, 45600000, 5000000)

        @JvmStatic
        fun inSwitzerland(point: LatLong): Boolean {
            return SWISS_AREA.contains(point)
        }
    }
}
