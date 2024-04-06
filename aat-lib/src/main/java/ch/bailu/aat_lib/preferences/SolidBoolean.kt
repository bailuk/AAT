package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.resources.Res

open class SolidBoolean(storage: StorageInterface, key: String) : SolidStaticIndexList(
    storage, key, label
) {
    var value: Boolean
        get() = index == 1
        set(v) {
            index = if (v) 1 else 0
        }

    val isEnabled: Boolean
        get() = value

    companion object {
        private val label = arrayOf(
            Res.str().off(),
            Res.str().on()
        )
    }
}
