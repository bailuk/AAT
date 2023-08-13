package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.resources.Res

class AutoPauseState(ti: TrackerInternals) : State(ti) {
    init {
        try {
            internal.logger.logPause()
            internal.statusIcon.showAutoPause()
        } catch (e: Exception) {
            internal.emergencyOff(e)
        }
    }

    override fun getStateID(): Int {
        return StateID.AUTO_PAUSED
    }

    override fun preferencesChanged() {
        if (!internal.isReadyForAutoPause) {
            internal.setState(OnState(internal))
        }
    }

    override fun updateTrack() {
        if (!internal.isReadyForAutoPause) {
            internal.setState(OnState(internal))
        }
    }

    override fun onStartPauseResume() {
        onPauseResume()
    }

    override fun onStartStop() {
        internal.setState(OffState(internal))
    }

    override fun onPauseResume() {
        internal.setState(PauseState(internal))
    }

    override fun getStartStopText(): String {
        return Res.str().tracker_stop()
    }

    override fun getPauseResumeText(): String {
        return Res.str().tracker_pause()
    }

    override fun getStartStopIcon(): String {
        return "playback_stop_inverse"
    }
}
