package ch.bailu.aat_lib.coordinates

import org.mapsforge.core.model.LatLong
import kotlin.math.roundToInt

abstract class MeterCoordinates : Coordinates() {
    /**
     *
     * @return northing part of coordinate in meters
     */
    abstract val northing: Int

    /**
     *
     * @return easting part of coordinate in meters
     */
    abstract val easting: Int

    /**
     *
     * @return WGS84 Latitude / Longitude representation of coordinate
     */
    abstract override fun toLatLong(): LatLong

    /**
     * round northing and easting to decimal place
     * @param dec decimal place to round to
     */
    abstract fun round(dec: Int)

    companion object {
        @JvmStatic
        fun round(value: Int, dec: Int): Int {
            return dec * (value.toFloat() / dec.toFloat()).roundToInt()
        }
    }
}
