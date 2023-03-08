package ch.bailu.aat_gtk.service.location

import ch.bailu.aat_lib.service.location.LocationInformation
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName
import ch.bailu.gtk.geoclue.Location
import org.mapsforge.core.model.LatLong

class GeoClue2LocationInformation(location: Location, private val _state: Int): LocationInformation() {

    private val _location = LatLong(location.latitude, location.longitude)
    private var _altitude = location.altitude
    private val _speed = location.speed.toFloat()
    private val _provider = FocName("GeoClue2")
    private val _accuracy = location.accuracy.toFloat()
    private val _timestamp = System.currentTimeMillis()

    override fun getState(): Int {
        return _state
    }

    override fun getFile(): Foc {
        return _provider
    }

    override fun getAccuracy(): Float {
        return _accuracy
    }

    override fun getSpeed(): Float {
        return _speed
    }

    override fun getAltitude(): Double {
        return _altitude
    }

    override fun getLatitude(): Double {
        return _location.getLatitude()
    }

    override fun getLongitude(): Double {
        return _location.getLongitude()
    }

    override fun getTimeStamp(): Long {
        return _timestamp
    }

    override fun getLatitudeE6(): Int {
        return (latitude * 1e6).toInt()
    }

    override fun getLongitudeE6(): Int {
        return (longitude * 1e6).toInt()
    }

    override fun hasAccuracy(): Boolean {
        return _accuracy > 0
    }

    override fun hasSpeed(): Boolean {
        return _speed > 0
    }

    override fun hasAltitude(): Boolean {
        return true
    }

    override fun hasBearing(): Boolean {
        return false
    }

    override fun isFromGPS(): Boolean {
        return true
    }

    override fun getCreationTime(): Long {
        return _timestamp
    }

    override fun setAltitude(altitude: Double) {
        _altitude = altitude
    }
}
