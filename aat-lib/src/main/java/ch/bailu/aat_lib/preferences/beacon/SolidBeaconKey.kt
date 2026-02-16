package ch.bailu.aat_lib.preferences.beacon

import ch.bailu.aat_lib.preferences.SolidLong
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidBeaconKey(storage: StorageInterface) : SolidLong(storage, KEY) {
    override fun getLabel(): String {
        return "Key";
    }

    companion object {
        const val KEY = "BEACON_KEY"
    }
}
