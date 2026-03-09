package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.information.GpxInformationProvider

interface TrackerServiceInterface : StateInterface,
    GpxInformationProvider {
    fun getPresetIndex(): Int

    fun resumeLogger(old: GpxList)
}
