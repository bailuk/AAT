package ch.bailu.aat_lib.dispatcher.source

import ch.bailu.aat_lib.dispatcher.SourceInterface
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Timer

class TrackerTimerSource(private val scontext: ServicesInterface, private val timer: Timer) :
    SourceInterface {
    private var target = TargetInterface.NULL

    override fun setTarget(target: TargetInterface) {
        this.target = target
    }

    override fun requestUpdate() {
        target.onContentUpdated(
            InfoID.TRACKER_TIMER,
            scontext.getTrackerService().getInfo()
        )
        timer.kick(INTERVAL.toLong()) { requestUpdate() }
    }

    override fun onPauseWithService() {
        timer.cancel()
    }

    override fun onDestroy() {}

    override fun onResumeWithService() {
        timer.kick(INTERVAL.toLong()) { requestUpdate() }
    }

    override fun getIID(): Int {
        return InfoID.TRACKER_TIMER
    }

    override fun getInfo(): GpxInformation {
        return scontext.getTrackerService().getInfo()
    }

    companion object {
        private const val INTERVAL = 500
    }
}
