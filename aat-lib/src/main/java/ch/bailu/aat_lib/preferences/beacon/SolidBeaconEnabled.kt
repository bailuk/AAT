package ch.bailu.aat_lib.preferences.beacon

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidBeaconEnabled(storage: StorageInterface) : SolidBoolean(storage, KEY) {
    override fun getLabel(): String {
        return "Enabled"
    }

    companion object {
        const val KEY = "BEACON_ENABLED"
    }
}
