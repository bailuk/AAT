package ch.bailu.aat_lib.service.beacon

import ch.bailu.aat_lib.gpx.information.GpxInformation

interface BeaconServiceInterface {
    fun getLoggerInformation(): GpxInformation
}
