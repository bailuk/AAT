package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.preferences.StorageInterface

class CleanLocation : LocationStackItem() {
    var loggableLocation: GpxInformation = GpxInformation.NULL
        private set
    private var creationTime: Long = 0

    fun hasLoggableLocation(lastLocation: GpxInformation): Boolean {
        return (loggableLocation !== lastLocation &&
                (System.currentTimeMillis() - creationTime) < LOCATION_LIFETIME_MILLIS)
    }

    override fun passState(state: Int) {}

    override fun passLocation(location: LocationInformation) {
        if (location.isFromGPS()) {
            loggableLocation = location
            creationTime = location.getCreationTime()
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {}

    companion object {
        private const val LOCATION_LIFETIME_MILLIS = (3 * 1000).toLong()
    }
}
