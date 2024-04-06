package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class SolidPositionLock(storage: StorageInterface, key: String) : SolidBoolean(storage, key + POSTFIX) {
    fun setDefaults() {
        value = true
    }

    override fun getIconResource(): String {
        return if (value) {
            "zoom_original_inverse"
        } else {
            "zoom_original"
        }
    }

    override fun getValueAsString(): String {
        return Res.str().tt_map_home()
    }

    override fun getToolTip(): String? {
        return Res.str().tt_map_home()
    }

    override fun getLabel(): String {
        return Res.str().location_title()
    }

    companion object {
        private const val POSTFIX = "_POSITION_LOCK"
    }
}
