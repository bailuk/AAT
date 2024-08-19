package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.resources.Res
import java.io.IOException

class OnState(tracker: TrackerInternals) : State(tracker) {
    private val attributes = AttributesCollector()
    private var location = GpxInformation.NULL

    init {
        if (tracker.isReadyForAutoPause) {
            tracker.setState(AutoPauseState(tracker))
        } else {
            tracker.statusIcon.showOn()
        }
    }

    override fun getStateID(): Int {
        return StateID.ON
    }

    override fun preferencesChanged() {}
    override fun updateTrack() {
        if (internal.isReadyForAutoPause) {
            internal.setState(AutoPauseState(internal))
        } else {
            val l = internal.services.getLocationService()
            try {
                val newLocation = l.getLoggableLocationOrNull(location)
                if (newLocation != null) {
                    location = newLocation
                    val attr = attributes.collect(internal.services.getSensorService())
                    internal.logger.log(location, attr)
                }
            } catch (e: IOException) {
                internal.emergencyOff(e)
            }
            internal.broadcaster.broadcast(AppBroadcaster.TRACKER)
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
        return "media_playback_stop_inverse"
    }
}
