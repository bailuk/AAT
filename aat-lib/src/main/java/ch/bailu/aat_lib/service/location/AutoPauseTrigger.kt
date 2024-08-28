package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.presets.SolidTrackerAutopause

/**
 * TODO remove
 */
class AutoPauseTrigger(next: LocationStackItem) : LocationStackChainedItem(next) {
    private var triggerSpeed = 0f
    private var trigger = Trigger(10)


    override fun close() {}

    override fun passLocation(location: LocationInformation) {
        val speed = location.getSpeed()
        if (speed < triggerSpeed) {
            trigger.down()
        } else {
            trigger.up()
        }
        super.passLocation(location)
    }

    val isAutoPaused: Boolean
        get() = trigger.isLow

    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {
        triggerSpeed = SolidTrackerAutopause(storage, presetIndex).triggerSpeed
        trigger = Trigger(SolidTrackerAutopause(storage, presetIndex).triggerLevel, trigger)
    }
}
