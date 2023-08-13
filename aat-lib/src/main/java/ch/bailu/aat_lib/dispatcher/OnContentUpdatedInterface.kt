package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformation

fun interface OnContentUpdatedInterface {
    fun onContentUpdated(iid: Int, info: GpxInformation)

    companion object {
        val NULL = OnContentUpdatedInterface { _, _ -> }
    }
}
