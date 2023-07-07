package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformationProvider

interface ContentSourceInterface : GpxInformationProvider {
    fun setTarget(target: OnContentUpdatedInterface)
    fun requestUpdate()
    fun onPause()
    fun onResume()
    fun getIID(): Int
}
