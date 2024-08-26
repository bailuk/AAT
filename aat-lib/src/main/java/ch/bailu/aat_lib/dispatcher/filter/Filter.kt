package ch.bailu.aat_lib.dispatcher.filter

import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.GpxInformation

class Filter(
    private val target: TargetInterface,
    private val usageTracker: UsageTrackerInterface) : TargetInterface {

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (usageTracker.isEnabled(iid)) {
            target.onContentUpdated(iid, info)
        } else {
            target.onContentUpdated(iid, GpxInformation.NULL)
        }
    }
}
