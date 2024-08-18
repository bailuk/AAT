package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.service.ServicesInterface

class TrackerSource(private val services: ServicesInterface, private val broadcaster: Broadcaster) :
    ContentSource() {
    private val onTrackChanged =
        BroadcastReceiver { sendUpdate(InfoID.TRACKER, services.trackerService.info) }

    override fun requestUpdate() {
        sendUpdate(InfoID.TRACKER, services.trackerService.info)
    }

    override fun onPause() {
        broadcaster.unregister(onTrackChanged)
    }

    override fun onResume() {
        broadcaster.register(AppBroadcaster.TRACKER, onTrackChanged)
    }

    override fun getIID(): Int {
        return InfoID.TRACKER
    }

    override fun getInfo(): GpxInformation {
        return services.trackerService.info
    }
}
