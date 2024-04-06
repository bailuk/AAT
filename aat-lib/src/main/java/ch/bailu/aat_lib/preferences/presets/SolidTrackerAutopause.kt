package ch.bailu.aat_lib.preferences.presets

import ch.bailu.aat_lib.preferences.SolidAutopause
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class SolidTrackerAutopause(storage: StorageInterface, index: Int) : SolidAutopause(
    storage, KEY, index) {
    override fun getLabel(): String {
        return Res.str().p_tracker_autopause()
    }

    companion object {
        private const val KEY = "autopause"
    }
}
