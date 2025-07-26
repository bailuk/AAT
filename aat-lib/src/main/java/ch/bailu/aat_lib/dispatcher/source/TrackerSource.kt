package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.dispatcher.SourceInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.service.ServicesInterface

class TrackerSource(
    private val services: ServicesInterface,
    private val broadcaster: Broadcaster,
    usageTracker: UsageTrackerInterface
) :
    SourceInterface
{
    private var target = TargetInterface.NULL

    init {
        usageTracker.observe {
            requestUpdate()
        }
    }

    override fun setTarget(target: TargetInterface) {
        this.target = target
    }

    private val onTrackChanged = BroadcastReceiver { requestUpdate() }

    override fun requestUpdate() {
        target.onContentUpdated(InfoID.TRACKER, services.getTrackerService().info)
    }

    override fun onPauseWithService() {
        broadcaster.unregister(onTrackChanged)
    }

    override fun onDestroy() {}

    override fun onResumeWithService() {
        broadcaster.register(AppBroadcaster.TRACKER, onTrackChanged)
    }

    override fun getIID(): Int {
        return InfoID.TRACKER
    }

    override fun getInfo(): GpxInformation {
        return services.getTrackerService().info
    }
}
