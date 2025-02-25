package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.gpx.information.GpxInformation


abstract class LocationInformation : GpxInformation() {
    abstract fun hasAccuracy(): Boolean
    abstract fun hasSpeed(): Boolean
    abstract fun hasAltitude(): Boolean
    abstract fun hasBearing(): Boolean

    abstract fun isFromGPS(): Boolean
    abstract fun getCreationTime(): Long

    abstract fun setAltitude(altitude: Double)
}
