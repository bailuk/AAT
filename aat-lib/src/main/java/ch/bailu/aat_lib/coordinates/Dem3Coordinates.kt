package ch.bailu.aat_lib.coordinates

import org.mapsforge.core.model.LatLong
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.floor

/**
 * Holds a specific geo location. Generates file path and url for the DEM3 tile
 * that is responsible for this geo location.
 *
 *
 * Digital Elevation Models in 3 arc / second resolution.
 * All tiles are taken from http://viewfinderpanoramas.org/dem3.html and repackaged.
 * Most tiles are originally from the 2000 Shuttle Radar Topography Mission.
 * See http://viewfinderpanoramas.org/dem3 and https://bailu.ch/dem3 for details.
 */
class Dem3Coordinates(la: Double, lo: Double) : Coordinates(), LatLongInterface {
    private val la: Int
    private val lo: Int
    private val string: String

    constructor(la: Int, lo: Int) : this(la / 1e6, lo / 1e6)
    constructor(p: LatLongInterface) : this(p.getLatitudeE6(), p.getLongitudeE6())
    constructor(p: LatLong) : this(p.getLatitude(), p.getLongitude())

    init {
        this.la = floor(la).toInt()
        this.lo = floor(lo).toInt()
        string = toLaString() + toLoString()
    }

    private fun toLaString(): String {
        return WGS84Coordinates.getLatitudeChar(la.toDouble()).toString() + f2.format(
            abs(la).toLong()
        )
    }

    private fun toLoString(): String {
        return WGS84Coordinates.getLongitudeChar(lo.toDouble()).toString() + f3.format(
            abs(lo).toLong()
        )
    }

    /**
     *
     * @return A base string for a file name of a dem3 tile. Example: "N16E077"
     */

    override fun toString(): String {
        return string
    }

    override fun getLatitudeE6(): Int {
        return la * 1e6.toInt()
    }

    override fun getLongitudeE6(): Int {
        return lo * 1e6.toInt()
    }

    override fun getLatitude(): Double {
        return la.toDouble()
    }

    override fun getLongitude(): Double {
        return lo.toDouble()
    }

    override fun equals(other: Any?): Boolean {
        return other is Dem3Coordinates && string == other.toString()
    }

    override fun hashCode(): Int {
        return string.hashCode()
    }

    /**
     *
     * @return A base string for a file name and directory of a dem3 tile. Example: "N16/N16E077"
     */
    fun toExtString(): String {
        return toLaString() + "/" + toString()
    }

    /**
     *
     * @return URL of dem3 tile. Example: https://bailu.ch/dem3/N16/N16E077.hgt.zip
     */
    fun toURL(): String {
        return BASE_URL + toExtString() + ".hgt.zip"
    }

    /**
     *
     * @return exact coordinates
     */
    override fun toLatLong(): LatLong {
        return LatLong(la.toDouble(), lo.toDouble())
    }


    companion object {
        private const val BASE_URL = "https://bailu.ch/dem3/"
        private val f2 = DecimalFormat("00")
        private val f3 = DecimalFormat("000")
    }
}
