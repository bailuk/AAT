package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.information.GpxInformation

fun interface TargetInterface {
    fun onContentUpdated(iid: Int, info: GpxInformation)

    companion object {
        val NULL = TargetInterface { _, _ -> }
    }
}
