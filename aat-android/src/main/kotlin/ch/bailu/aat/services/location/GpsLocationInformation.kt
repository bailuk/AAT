package ch.bailu.aat.services.location

import android.location.Location

class GpsLocationInformation(location: Location, state: Int) : RealLocationInformation(location, state) {
    private val time = System.currentTimeMillis()

    override fun getCreationTime(): Long {
        return time
    }

    override fun isFromGPS(): Boolean {
        return true
    }
}
