package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformation

abstract class ContentSource : SourceInterface {
    private var target = TargetInterface.NULL

    override fun setTarget(target: TargetInterface) {
        this.target = target
    }

    fun sendUpdate(iid: Int, info: GpxInformation) {
        target.onContentUpdated(iid, info)
    }
}
