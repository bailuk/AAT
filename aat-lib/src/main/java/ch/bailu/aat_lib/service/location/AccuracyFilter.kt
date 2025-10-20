package ch.bailu.aat_lib.service.location

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.presets.SolidAccuracyFilter

/**
 * Only pass location if accuracy is more precise than min accuracy.
 * Accuracy represents a diameter of a circle. Unit is meter
 */
class AccuracyFilter(n: LocationStackItem) : LocationStackChainedItem(n) {
    private var minAccuracy = 99f

    override fun close() {}

    override fun passLocation(location: LocationInformation) {
        if (location.getAccuracy() < minAccuracy) {
            super.passLocation(location)
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int) {
        minAccuracy = SolidAccuracyFilter(storage, presetIndex).minAccuracy
    }
}
