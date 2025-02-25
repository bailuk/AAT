package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.presets.SolidMissingTrigger
import javax.annotation.Nonnull

class MissingTrigger(next: LocationStackItem) : LocationStackChainedItem(next) {
    private var triggerMillis = 15000
    private var stamp = System.currentTimeMillis()

    override fun passLocation(@Nonnull location: LocationInformation) {
        stamp = location.getTimeStamp()
        super.passLocation(location)
    }

    val isMissingUpdates: Boolean
        get() = System.currentTimeMillis() - stamp > triggerMillis

    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {
        triggerMillis = SolidMissingTrigger(storage, presetIndex).triggerMillis
    }
}
