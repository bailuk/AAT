package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import org.mapsforge.core.model.LatLong

class GpxPoint : GpxPointInterface {
    private var altitude: Float
    private val longitude: Int
    private val latitude: Int
    private val timestamp: Long

    private constructor() {
        altitude = 0f
        longitude = 0
        latitude = 0
        timestamp = 0
    }

    constructor(tp: GpxPointInterface) {
        altitude = tp.getAltitude().toFloat()
        longitude = tp.getLongitudeE6()
        latitude = tp.getLatitudeE6()
        timestamp = tp.getTimeStamp()
    }

    constructor(p: LatLong, a: Float, time: Long) {
        latitude = p.latitudeE6
        longitude = p.longitudeE6
        altitude = a
        timestamp = time
    }

    constructor(gp: LatLongInterface, a: Float, time: Long) {
        latitude = gp.getLatitudeE6()
        longitude = gp.getLongitudeE6()
        altitude = a
        timestamp = time
    }

    override fun getLatitude(): Double {
        return (getLatitudeE6().toDouble()) / 1E6
    }

    override fun getLongitude(): Double {
        return (getLongitudeE6().toDouble()) / 1E6
    }

    override fun getTimeStamp(): Long {
        return timestamp
    }

    override fun getLatitudeE6(): Int {
        return latitude
    }

    override fun getLongitudeE6(): Int {
        return longitude
    }

    override fun getAltitude(): Float {
        return altitude
    }

    fun setAltitude(e: Float) {
        altitude = e
    }

    override fun getAttributes(): GpxAttributes {
        return GpxAttributesNull.NULL
    }

    companion object {
        const val SIZE_IN_BYTES: Long = ((4) + (2 * 4) + (8)).toLong()

        @JvmField
        val NULL: GpxPoint = GpxPoint()
    }
}
