package ch.bailu.aat_lib.preferences.general

import ch.bailu.aat_lib.preferences.SolidAutopause
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class SolidPostprocessedAutopause(storage: StorageInterface, preset: Int) : SolidAutopause(
    storage, KEY, preset
) {
    override fun getLabel(): String {
        return Res.str().p_post_autopause()
    }

    companion object {
        protected const val KEY = "pautopause"
    }
}
