package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.SolidAutopause
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.preferences.presets.SolidTrackerAutopause
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.service.ServicesInterface
import java.io.Closeable
import java.io.IOException

class TrackerInternals(
    private val sdirectory: SolidDataDirectory,
    val statusIcon: StatusIconInterface,
    val broadcaster: Broadcaster,
    val services: ServicesInterface
) : OnPreferencesChanged, Closeable {
    private var state: State
    @JvmField
    var logger: Logger
    private var sautopause: SolidAutopause? = null
    @JvmField
    var presetIndex = 0

    init {
        rereadPreferences()
        try {
            logger = createLogger()
            logger.close()
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
        logger = Logger.NULL_LOGGER
        state = OffState(this)
        sdirectory.getStorage().register(this)
    }

    fun setState(state: State) {
        this.state = state
        broadcaster.broadcast(AppBroadcaster.TRACKER)
    }

    fun getState(): State {
        return state
    }

    fun rereadPreferences() {
        presetIndex = SolidPreset(sdirectory.getStorage()).index
        sautopause = SolidTrackerAutopause(sdirectory.getStorage(), presetIndex)
        services.locationService.setPresetIndex(presetIndex)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        state.preferencesChanged()
    }

    fun lockService() {
        services.lock(this.javaClass.simpleName)
    }

    fun unlockService() {
        services.free(this.javaClass.simpleName)
    }

    @Throws(IOException::class, SecurityException::class)
    fun createLogger(): TrackLogger {
        return TrackLogger(sdirectory, SolidPreset(sdirectory.getStorage()).index)
    }

    val isReadyForAutoPause: Boolean
        get() = (services.locationService.isMissingUpdates ||
                services.locationService.isAutopaused) &&
                sautopause!!.isEnabled

    fun emergencyOff(e: Exception?) {
        AppLog.e(this, e)
        logger.close()
        logger = Logger.NULL_LOGGER
        state = OffState(this)
    }

    override fun close() {
        logger.close()
        sdirectory.getStorage().unregister(this)
    }
}
