package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.gpx.GpxDeltaHelper
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidDistanceFilter
import javax.annotation.Nonnull

class DistanceFilter(next: LocationStackItem) : LocationStackChainedItem(next) {
    private var oldLocation: LocationInformation? = null
    private var minDistance = 0f

    override fun close() {}

    override fun passLocation(location: LocationInformation) {
        val oldLocation = this.oldLocation

        if (oldLocation == null || notTooClose(oldLocation, location)) {
            this.oldLocation = location
            super.passLocation(location)
        }
    }

    private fun notTooClose(a: LocationInformation, b: LocationInformation): Boolean {
        return if (minDistance > 90) {
            GpxDeltaHelper.getDistance(a, b) >= (a.accuracy + b.accuracy) / 2
        } else {
            GpxDeltaHelper.getDistance(a, b) >= minDistance
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {
        minDistance = SolidDistanceFilter(storage, presetIndex).minDistance
    }
}
