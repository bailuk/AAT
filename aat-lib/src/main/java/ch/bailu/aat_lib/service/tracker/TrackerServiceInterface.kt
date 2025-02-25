package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.gpx.information.GpxInformationProvider

interface TrackerServiceInterface : StateInterface,
    GpxInformationProvider {
    fun getPresetIndex(): Int
}
