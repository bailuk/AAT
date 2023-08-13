package ch.bailu.aat_lib.coordinates

import org.mapsforge.core.model.LatLong
import org.mapsforge.core.util.LatLongUtils
import javax.annotation.Nonnull

class LatLongE6 : LatLongInterface {
    private val latitudeE6: Int
    private val longitudeE6: Int

    constructor(latitude: Int, longitude: Int) {
        latitudeE6 = latitude
        longitudeE6 = longitude
    }

    constructor(latitude: Double, longitude: Double) {
        latitudeE6 = toE6(latitude)
        longitudeE6 = toE6(longitude)
    }

    constructor(p: LatLong) {
        latitudeE6 = toE6(p.latitude)
        longitudeE6 = toE6(p.longitude)
    }

    fun toLatLong(): LatLong {
        return LatLong(toD(latitudeE6), toD(longitudeE6))
    }

    override fun getLatitudeE6(): Int {
        return latitudeE6
    }

    override fun getLongitudeE6(): Int {
        return longitudeE6
    }

    override fun getLatitude(): Double {
        return toD(latitudeE6)
    }

    override fun getLongitude(): Double {
        return toD(longitudeE6)
    }

    @Nonnull
    override fun toString(): String {
        return toLatLong().toString()
    }

    companion object {
        @JvmStatic
        fun toD(value: Int): Double {
            return LatLongUtils.microdegreesToDegrees(value)
        }

        fun toE6(value: Double): Int {
            return LatLongUtils.degreesToMicrodegrees(value)
        }

        @JvmStatic
        fun toLatLong(latLong: LatLongInterface): LatLong {
            return LatLong(toD(latLong.getLatitudeE6()), toD(latLong.getLongitudeE6()))
        }
    }
}
