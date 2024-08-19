package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.util.WithStatusText

class TrackerService(
    sdirectory: SolidDataDirectory,
    statusIconInterface: StatusIconInterface,
    private val broadcaster: Broadcaster,
    servicesInterface: ServicesInterface
) : VirtualService(), WithStatusText, TrackerServiceInterface {
    private val internal =
        TrackerInternals(sdirectory, statusIconInterface, broadcaster, servicesInterface)


    @Synchronized
    override fun getInfo(): GpxInformation {
        return internal.logger
    }


    private val onLocation = BroadcastReceiver { internal.getState().updateTrack() }


    init {
        broadcaster.register(AppBroadcaster.LOCATION_CHANGED, onLocation)
    }


    @Synchronized
    override fun getPresetIndex(): Int {
        return internal.presetIndex
    }


    @Synchronized
    override fun appendStatusText(builder: StringBuilder) {
        builder.append("Log to: ")
            .append(internal.logger.getFile().pathName)
    }

    @Synchronized
    override fun close() {
        internal.close()
        broadcaster.unregister(onLocation)
    }

    @Synchronized
    override fun updateTrack() {
        internal.getState().updateTrack()
    }

    @Synchronized
    override fun onStartPauseResume() {
        internal.getState().onStartPauseResume()
    }

    @Synchronized
    override fun onStartStop() {
        internal.getState().onStartStop()
    }

    @Synchronized
    override fun onPauseResume() {
        internal.getState().onPauseResume()
    }

    @Synchronized
    override fun getStateID(): Int {
        return internal.getState().getStateID()
    }

    @Synchronized
    override fun getStartStopText(): String {
        return internal.getState().getStartStopText()
    }

    @Synchronized
    override fun getPauseResumeText(): String {
        return internal.getState().getPauseResumeText()
    }

    @Synchronized
    override fun getStartStopIcon(): String {
        return internal.getState().getStartStopIcon()
    }
}
