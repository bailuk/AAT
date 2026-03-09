package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.preferences.StorageInterface

/** A {@link GpxWindow} that trims when accumulated distance exceeds a limit derived from total track length. */
class GpxDistanceWindow(list: GpxList) : GpxWindow(list.pointList.first as GpxPointNode?) {
    private val distanceLimit: Float = getDistanceLimit(list.getDelta().getDistance()).toFloat()

    override fun overLimit(): Boolean {
        return getDistance() > distanceLimit
    }

    fun getLimitAsString(s: StorageInterface): String {
        if (distanceLimit > 0) return DistanceDescription(s).getDistanceDescription(distanceLimit)
        return ""
    }

    companion object {
        private fun getDistanceLimit(distance: Float): Int {
            if (distance > 60000) return 2000
            if (distance > 30000) return 1000
            if (distance > 10000) return 500
            if (distance > 5000) return 100
            if (distance > 2000) return 50
            if (distance > 1000) return 10
            if (distance > 0) return 5
            return 0
        }
    }
}
