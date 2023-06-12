package ch.bailu.aat.preferences.map

import ch.bailu.aat_lib.preferences.SolidStaticIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

/**
 * TODO move to lib
 */
class SolidTrimMode(storageInterface: StorageInterface?) : SolidStaticIndexList(
    storageInterface,
    SolidTrimMode::class.java.simpleName,
    generateModes()
) {
    override fun length(): Int {
        return modes.size
    }

    override fun getLabel(): String {
        return Res.str().p_trim_mode()
    }

    override fun getValueAsString(i: Int): String {
        return modes[i]
    }

    companion object {
        const val MODE_TO_SIZE = 0
        const val MODE_TO_SIZE_AND_AGE = 1
        const val MODE_TO_AGE = 2
        const val MODE_TO_SIZE_OR_AGE = 3

        private var modes: Array<String> = arrayOf()

        fun generateModes(): Array<String> {
            if (modes.isEmpty()) {
                modes = Res.str().p_trim_modes()
            }
            return modes
        }
    }
}
