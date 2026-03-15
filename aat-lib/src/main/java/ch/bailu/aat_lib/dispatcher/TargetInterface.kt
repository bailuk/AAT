package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.information.GpxInformation

/**
 * Receiver of [GpxInformation] updates routed through [Dispatcher].
 *
 * Implemented by [NumberView] (UI layer) and [ContentDescription] (formatting
 * layer). The [iid] parameter identifies the data source (see [InfoID]) so
 * implementations can filter for relevant updates.
 */
fun interface TargetInterface {
    fun onContentUpdated(iid: Int, info: GpxInformation)

    companion object {
        val NULL = TargetInterface { _, _ -> }
    }
}
