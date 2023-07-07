package ch.bailu.aat_lib.gpx.interfaces

import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes

interface GpxPointInterface : LatLongInterface {
    fun getAltitude(): Double
    override fun getLongitude(): Double
    override fun getLatitude(): Double
    fun getTimeStamp(): Long
    fun getAttributes(): GpxAttributes
}
