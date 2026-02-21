package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.util.LatLongUtils

object GpxDeltaHelper {
    @JvmStatic
    fun getDistance(a: GpxPointInterface, b: GpxPointInterface): Float {
        return getDistance(
            LatLong(a.getLatitude(), a.getLongitude()),
            LatLong(b.getLatitude(), b.getLongitude())
        )
    }

    fun getDistance(a: LatLong, b: LatLong): Float {
        return LatLongUtils.sphericalDistance(a, b).toFloat()
    }

    @JvmStatic
    fun getAcceleration(a: GpxDeltaPointInterface, b: GpxDeltaPointInterface): Float {
        val deltaSpeed = b.getSpeed() - a.getSpeed()
        val deltaTime = getTimeDeltaSI(a, b)
        return getAcceleration(deltaSpeed, deltaTime)
    }

    private fun getAcceleration(deltaSpeed: Float, deltaTime: Float): Float {
        return if (deltaTime != 0f) deltaSpeed / deltaTime
        else 0f
    }

    fun getSpeed(a: GpxPointInterface, b: GpxPointInterface): Float {
        return getSpeed(getDistance(a, b), getTimeDeltaSI(a, b))
    }

    @JvmStatic
    fun getSpeed(distance: Float, time: Float): Float {
        return if (time > 0f) (distance / time)
        else 0f
    }

    @JvmStatic
    fun getTimeDeltaMilli(a: GpxPointInterface, b: GpxPointInterface): Long {
        return b.getTimeStamp() - a.getTimeStamp()
    }

    @JvmStatic
    fun getTimeDeltaSI(a: GpxPointInterface, b: GpxPointInterface): Float {
        val deltaT = getTimeDeltaMilli(a, b).toFloat()
        return deltaT / 1000f
    }
}
