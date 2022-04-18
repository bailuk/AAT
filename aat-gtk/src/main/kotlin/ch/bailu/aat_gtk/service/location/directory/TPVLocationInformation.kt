package ch.bailu.aat_gtk.service.location.directory

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.coordinates.LatLongE6
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.service.location.LocationInformation
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName
import de.taimos.gpsd4java.types.TPVObject

class TPVLocationInformation(private val tpv: TPVObject) : LocationInformation() {

    override fun getAltitude(): Double {
        return tpv.altitude
    }

    override fun getLatitude(): Double {
        return tpv.latitude
    }

    override fun getLatitudeE6(): Int {
        return LatLongE6.toE6(tpv.latitude)
    }

    override fun getLongitudeE6(): Int {
        return LatLongE6.toE6(tpv.longitude)
    }

    override fun getLongitude(): Double {
        return tpv.longitude
    }

    override fun getState(): Int {
        return StateID.ON
    }

    override fun getFile(): Foc {
        return FocName("GPSd")
    }

    override fun getTimeStamp(): Long {
        return tpv.timestamp.toLong()
    }

    override fun getDistance(): Float {
        return 0f
    }

    override fun getSpeed(): Float {
        return tpv.speed.toFloat()
    }

    override fun getAcceleration(): Float {
        return 0f
    }

    override fun getTimeDelta(): Long {
        return 0
    }

    override fun getBoundingBox(): BoundingBoxE6? {
        return BoundingBoxE6.NULL_BOX
    }

    override fun hasAccuracy(): Boolean {
        return true
    }

    override fun hasSpeed(): Boolean {
        return true
    }

    override fun hasAltitude(): Boolean {
        return true
    }

    override fun hasBearing(): Boolean {
        return true
    }

    override fun isFromGPS(): Boolean {
        return true
    }

    override fun getCreationTime(): Long {
        return timeStamp
    }

    override fun setAltitude(altitude: Double) {}

    override fun getAccuracy(): Float {
        return 5f
    }
}
