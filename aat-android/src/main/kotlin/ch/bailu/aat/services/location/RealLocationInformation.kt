package ch.bailu.aat.services.location

import android.location.Location
import ch.bailu.aat_lib.service.location.LocationInformation
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName

open class RealLocationInformation(private val location: Location, private val state: Int) : LocationInformation() {
    private val provider: FocName = FocName(location.provider)

    override fun getState(): Int {
        return state
    }

    override fun getFile(): Foc {
        return provider
    }

    override fun getAccuracy(): Float {
        return location.accuracy
    }

    override fun getSpeed(): Float {
        return location.speed
    }

    override fun getAltitude(): Double {
        return location.altitude
    }

    override fun getLatitude(): Double {
        return location.latitude
    }

    override fun getLongitude(): Double {
        return location.longitude
    }

    override fun getTimeStamp(): Long {
        return location.time
    }

    override fun getLatitudeE6(): Int {
        return (latitude * 1e6).toInt()
    }

    override fun getLongitudeE6(): Int {
        return (longitude * 1e6).toInt()
    }

    override fun hasAccuracy(): Boolean {
        return location.hasAccuracy()
    }

    override fun hasSpeed(): Boolean {
        return location.hasSpeed()
    }

    override fun hasAltitude(): Boolean {
        return location.hasAltitude()
    }

    override fun hasBearing(): Boolean {
        return location.hasBearing()
    }

    override fun isFromGPS(): Boolean {
        return false
    }

    override fun getCreationTime(): Long {
        return timeStamp
    }

    override fun setAltitude(altitude: Double) {
        location.altitude = altitude
    }
}
