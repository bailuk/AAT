package ch.bailu.aat.services.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.location.SolidGpsTimeFix
import ch.bailu.aat.preferences.location.SolidGpsTimeFix.Companion.fix
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.location.LocationInformation
import ch.bailu.aat_lib.service.location.LocationStackItem

class GpsLocation(i: LocationStackItem?, context: Context, interval: Int) :
    RealLocation(i, context, LocationManager.GPS_PROVIDER, interval) {
    private var fixTime: Boolean = SolidGpsTimeFix(Storage(context)).value


    override fun factoryLocationInformation(location: Location, state: Int): LocationInformation {
        val result: LocationInformation = GpsLocationInformation(location, state)
        fixGpsTime(location, result.creationTime)
        return result
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {
        fixTime = SolidGpsTimeFix(storage).value
    }

    private fun fixGpsTime(location: Location, systemTime: Long) {
        val time = fix(location.time, systemTime)
        if (fixTime) {
            location.time = time
        }
    }
}
