package ch.bailu.aat_lib.preferences.beacon

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.ToDo

class SolidBeaconEnabled(storage: StorageInterface) : SolidBoolean(storage, KEY) {
    override fun getLabel(): String {
        return "Enabled"
    }

    override fun getToolTip(): String? {
        return toolTip
    }

    companion object {
        const val KEY = "BEACON_ENABLED"
        val toolTip = ToDo.translate("Experimental: Service for sharing live GPS locations. See https://github.com/MaxKellermann/beacon")
    }
}
