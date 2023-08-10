package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.resources.Res

class OffState(ti: TrackerInternals?) : State(ti!!) {
    init {
        internal.logger.close()
        internal.statusIcon.hide()
        internal.unlockService()
        internal.rereadPreferences()
    }

    override fun updateTrack() {}
    override fun getStateID(): Int {
        return StateID.OFF
    }

    override fun onStartPauseResume() {
        onStartStop()
    }

    override fun onStartStop() {
        try {
            internal.logger = internal.createLogger()
            internal.lockService()
            internal.setState(OnState(internal))
        } catch (e: Exception) {
            AppLog.e(this, e)
            internal.logger = Logger.NULL_LOGGER
        }
    }

    override fun onPauseResume() {}
    override fun getStartStopText(): String {
        return Res.str().tracker_start()
    }

    override fun getPauseResumeText(): String {
        return Res.str().tracker_start()
    }

    override fun getStartStopIcon(): String {
        return "playback_start_inverse"
    }

    override fun preferencesChanged() {
        internal.rereadPreferences()
    }
}
