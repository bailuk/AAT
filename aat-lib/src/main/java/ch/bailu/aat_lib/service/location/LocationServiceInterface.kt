package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.gpx.GpxInformation

interface LocationServiceInterface {
    fun setPresetIndex(presetIndex: Int)

    fun isMissingUpdates(): Boolean
    fun isAutoPaused(): Boolean

    fun getLoggableLocationOrNull(old: GpxInformation): GpxInformation?
    fun getLocationInformation(): GpxInformation
}
