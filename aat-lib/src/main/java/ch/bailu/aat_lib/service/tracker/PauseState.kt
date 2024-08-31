package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.gpx.information.StateID
import ch.bailu.aat_lib.resources.Res

class PauseState(trackerInternals: TrackerInternals) : State(trackerInternals) {
    init {
        try {
            internal.logger.logPause()
            internal.statusIcon.showPause()
        } catch (e: Exception) {
            internal.emergencyOff(e)
        }
    }

    override fun updateTrack() {}
    override fun getStateID(): Int {
        return StateID.PAUSE
    }

    override fun onStartPauseResume() {
        onPauseResume()
    }

    override fun onStartStop() {
        internal.setState(OffState(internal))
    }

    override fun onPauseResume() {
        internal.setState(OnState(internal))
    }

    override fun getStartStopText(): String {
        return Res.str().tracker_stop()
    }

    override fun getPauseResumeText(): String {
        return Res.str().tracker_resume()
    }

    override fun getStartStopIcon(): String {
        return "media_playback_stop_inverse"
    }
}
