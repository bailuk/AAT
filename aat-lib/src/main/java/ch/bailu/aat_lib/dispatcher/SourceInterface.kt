package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.information.GpxInformationProvider

/**
 * Provider of [GpxInformation] that pushes updates into a [Dispatcher].
 *
 * A source is registered via [Dispatcher.addSource], which calls [setTarget]
 * to wire the source's output to the dispatcher. When new data is available
 * the source calls `target.onContentUpdated(getIID(), getInfo())`.
 * [requestUpdate] forces an immediate push of the current state.
 */
interface SourceInterface : GpxInformationProvider, EmitterInterface {
    fun setTarget(target: TargetInterface)
    fun getIID(): Int
}
