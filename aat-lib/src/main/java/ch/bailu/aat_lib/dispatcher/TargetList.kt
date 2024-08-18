package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformation

class TargetList : OnContentUpdatedInterface {
    private val targets = ArrayList<OnContentUpdatedInterface>(10)
    override fun onContentUpdated(iid: Int,  info: GpxInformation) {
        for (target in targets) {
            target.onContentUpdated(iid, info)
        }
    }

    fun add(t: OnContentUpdatedInterface) {
        targets.add(t)
    }
}
