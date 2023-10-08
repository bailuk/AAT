package ch.bailu.aat_lib.preferences.system

import ch.bailu.aat_lib.preferences.SolidLong
import ch.bailu.aat_lib.preferences.StorageInterface

class SolidStartCount(s: StorageInterface) : SolidLong(s, KEY) {

    val isFirstRun: Boolean
        get() = getValue() == 0L

    fun increment() {
        setValue(getValue() + 1)
    }

    companion object {
        private const val KEY = "start_count"
    }
}
