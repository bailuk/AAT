package ch.bailu.aat_lib.coordinates

import org.mapsforge.core.model.LatLong
import org.mapsforge.core.util.LatLongUtils

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


    override fun toString(): String {
        return toLatLong().toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as LatLongE6

        if (latitudeE6 != other.latitudeE6) return false
        if (longitudeE6 != other.longitudeE6) return false
        return true
    }

    override fun hashCode(): Int {
        var result = latitudeE6
        result = 31 * result + longitudeE6
        return result
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
