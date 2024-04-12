package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformationProvider

interface GpxInformationSource : GpxInformationProvider {
    fun isEnabled(): Boolean
    fun setEnabled(isEnabled: Boolean)
}
