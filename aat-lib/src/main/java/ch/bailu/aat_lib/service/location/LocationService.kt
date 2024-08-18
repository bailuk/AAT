package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.OnPresetPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.util.WithStatusText
import java.io.Closeable

/**
 * Maintains a stack of location devices and filters. Will broadcast location changes.
 * Provides two locations:
 * - Clean: Loggable location with precision according to user settings
 * - Dirty: Latest available location update. Can be from network or saved from last app run.
 */
class LocationService(
    private val sprovider: SolidLocationProvider,
    broadcastInterface: Broadcaster,
    sensorService: SensorServiceInterface
) : VirtualService(), LocationServiceInterface, Closeable, OnPresetPreferencesChanged,
    WithStatusText, OnPreferencesChanged {
    private val itemList = ArrayList<LocationStackItem>()

    private var provider: LocationStackItem? = null
    private val clean: CleanLocation
    private val dirty: DirtyLocation
    private val missing: MissingTrigger
    private val autoPause: AutoPauseTrigger

    private var presetIndex = 0

    init {
        sprovider.register(this)

        clean = CleanLocation().apply { itemList.add(this) }

        itemList.add(DistanceFilter(lastItem()))

        autoPause = AutoPauseTrigger(lastItem()).apply { itemList.add(this) }
        missing = MissingTrigger(lastItem()).apply { itemList.add(this) }
        itemList.add(AccuracyFilter(lastItem()))
        itemList.add(InformationFilter(lastItem()))

        dirty = DirtyLocation(lastItem(), sprovider.getStorage(), broadcastInterface).apply { itemList.add(this) }

        itemList.add(AltitudeFromBarometer(lastItem(), sensorService))
        itemList.add(AdjustGpsAltitude(lastItem(), sprovider.getStorage()))
        createLocationProvider()

        setPresetIndex(SolidPreset(sprovider.getStorage()).index)
    }


    @Synchronized
    override fun setPresetIndex(i: Int) {
        presetIndex = i
        onPreferencesChanged(sprovider.getStorage(), SolidPreset.KEY, presetIndex)
    }


    private fun lastItem(): LocationStackItem {
        val lastIndex = itemList.lastIndex
        return if (lastIndex > -1) {
            itemList[lastIndex]
        } else {
            CleanLocation()
        }
    }

    private fun createLocationProvider() {
        val oldProvider = provider

        if (oldProvider is LocationStackItem) {
            if (itemList.remove(oldProvider)) {
                oldProvider.close()
            }
        }

        val newProvider =  sprovider.createProvider(this, lastItem())
        this.provider = newProvider
        itemList.add(newProvider)
    }

    @Synchronized
    override fun close() {
        itemList.forEach { it.close() }
        sprovider.unregister(this)
    }

    @Synchronized
    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {
        itemList.forEach { it.onPreferencesChanged(storage, key, presetIndex) }
    }

    @Synchronized
    override fun getLocationInformation(): GpxInformation {
        return dirty.locationInformation
    }

    @Synchronized
    override fun getLoggableLocationOrNull(old: GpxInformation): GpxInformation? {
        if (hasLoggableLocation(old)) {
            return loggableLocation
        }
        return null
    }

    private val loggableLocation: GpxInformation
        get() = clean.loggableLocation

    private fun hasLoggableLocation(old: GpxInformation): Boolean {
        return clean.hasLoggableLocation(old)
    }

    @Synchronized
    override fun isAutoPaused(): Boolean {
        return autoPause.isAutoPaused
    }

    @Synchronized
    override fun isMissingUpdates(): Boolean {
        return missing.isMissingUpdates
    }

    @Synchronized
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (sprovider.hasKey(key)) createLocationProvider()

        onPreferencesChanged(storage, key, presetIndex)
    }

    @Synchronized
    override fun appendStatusText(builder: StringBuilder) {
        for (i in itemList.indices) itemList[i].appendStatusText(builder)
    }

    companion object {
        const val INITIAL_STATE: Int = StateID.WAIT
    }
}
