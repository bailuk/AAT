package ch.bailu.aat.services.sensor

import ch.bailu.aat_lib.gpx.GpxInformation

interface SensorInterface {
    fun getInformation(iid: Int): GpxInformation?
    val name: String
    fun close()
}
