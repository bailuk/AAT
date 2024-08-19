package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Timer

class TrackerTimerSource(private val scontext: ServicesInterface, private val timer: Timer) :
    ContentSource() {
    override fun requestUpdate() {
        sendUpdate(
            InfoID.TRACKER_TIMER,
            scontext.getTrackerService().info
        )
        timer.kick(INTERVAL.toLong()) { requestUpdate() }
    }

    override fun onPause() {
        timer.cancel()
    }

    override fun onResume() {
        timer.kick(INTERVAL.toLong()) { requestUpdate() }
    }

    override fun getIID(): Int {
        return InfoID.TRACKER_TIMER
    }

    override fun getInfo(): GpxInformation {
        return scontext.getTrackerService().info
    }

    companion object {
        private const val INTERVAL = 500
    }
}
