package ch.bailu.aat_lib.service.sensor

import ch.bailu.aat_lib.gpx.GpxInformation

interface SensorServiceInterface {
    fun getInformationOrNull(infoID: Int): GpxInformation?
    fun getInfo(iid: Int): GpxInformation
    fun updateConnections()
    fun scan()
}
