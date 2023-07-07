package ch.bailu.aat_lib.coordinates

import ch.bailu.aat_lib.logger.AppLog
import org.mapsforge.core.model.LatLong
import java.text.DecimalFormat
import javax.annotation.Nonnull
import kotlin.math.abs
import kotlin.math.roundToInt

class WGS84Coordinates : Coordinates {
    /**
     * WGS84 Sexagesimal and decimal operations
     */
    class Sexagesimal {
        val degree: Int
        val minute: Int
        val second: Double
        val decimal: Double

        constructor(deg: Int, min: Int, sec: Double) {
            degree = deg
            minute = min
            second = sec
            var coord = deg.toDouble()
            coord += min.toDouble() / 60.0
            coord += sec / 60.0 / 60.0
            decimal = coord
        }

        constructor(coordinate: Double) {
            var c = coordinate
            decimal = c
            degree = c.toInt()
            c = (abs(c) - abs(degree)) * 60.0
            minute = c.toInt()
            second = (c - minute) * 60.0
        }

        fun toE6(): Int {
            return (decimal * 1e6).roundToInt()
        }

        @Nonnull
        override fun toString(): String {
            return (fX.format(abs(degree).toLong()) + "\u00B0 "
                    + f00.format(minute.toLong()) + "\u0027 "
                    + f00.format(second) + "\u0027\u0027")
        }

        companion object {
            private val fX = DecimalFormat("#")
            private val f00 = DecimalFormat("00")
        }
    }

    val longitude: Sexagesimal
    val latitude: Sexagesimal

    constructor(point: LatLong) : this(point.getLatitude(), point.getLongitude())
    constructor(la: Double, lo: Double) {
        latitude = Sexagesimal(la)
        longitude = Sexagesimal(lo)
    }

    constructor(code: String) {
        val parts = code.split("[:,?#]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var scanLa = true
        var scanned = false
        var la = 0.0
        var lo = 0.0
        for (p in parts) {
            try {
                val d = p.trim { it <= ' ' }.toDouble()
                if (scanLa) {
                    la = d
                    scanLa = false
                } else {
                    lo = d
                    scanned = true
                }
            } catch (e: NumberFormatException) {
                AppLog.d(this, "$code: $p")
            }
        }
        if (scanned) {
            latitude = Sexagesimal(la)
            longitude = Sexagesimal(lo)
        } else {
            throw createIllegalCodeException(code)
        }
    }

    @Nonnull
    override fun toString(): String {
        return (latitude.toString() + " "
                + latitudeChar + " "
                + longitude.toString() + " "
                + longitudeChar)
    }

    private val latitudeChar: Char
        get() = getLatitudeChar(latitude.decimal)
    private val longitudeChar: Char
        get() = getLongitudeChar(longitude.decimal)

    override fun toLatLong(): LatLong {
        return LatLong(latitude.decimal, longitude.decimal)
    }


    companion object {
        fun getLatitudeChar(la: Double): Char {
            return if (la < 0) 'S' else 'N'
        }

        fun getLongitudeChar(lo: Double): Char {
            return if (lo < 0) 'W' else 'E'
        }

        @JvmStatic
        fun getGeoUri(src: LatLong): String {
            return "geo:" +
                    src.getLatitude() +
                    ',' +
                    src.getLongitude()
        }

        fun getGeoPointDescription(src: LatLong): String {
            return """
                   Coordinates:
                   Latitude:${src.getLatitude()}Longitude:${src.getLongitude()}
                   """.trimIndent()
        }
    }
}
