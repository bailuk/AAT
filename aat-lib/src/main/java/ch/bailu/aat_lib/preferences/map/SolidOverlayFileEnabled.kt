package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidOverlayFileEnabled(storage: StorageInterface, iid: Int) : SolidBoolean(
    storage, KEY_ENABLED + iid
) {
    companion object {
        private const val KEY_ENABLED = "overlay_enabled_"
    }
}
