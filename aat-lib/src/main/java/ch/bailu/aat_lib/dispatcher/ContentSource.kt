package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformation

abstract class ContentSource : ContentSourceInterface {
    private var target = OnContentUpdatedInterface.NULL

    override fun setTarget(target: OnContentUpdatedInterface) {
        this.target = target
    }

    fun sendUpdate(iid: Int, info: GpxInformation) {
        target.onContentUpdated(iid, info)
    }
}
