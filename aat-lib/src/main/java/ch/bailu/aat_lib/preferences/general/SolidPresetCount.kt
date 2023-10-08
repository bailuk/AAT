package ch.bailu.aat_lib.preferences.general

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import javax.annotation.Nonnull

class SolidPresetCount(storage: StorageInterface) : SolidIndexList(storage, KEY) {
    fun getValue(v: Int): Int {
        return if (v == 0) DEFAULT else MIN + v - 1
    }

    val value: Int
        get() = getValue(index)

    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_preset_slots()
    }

    override fun getToolTip(): String? {
        return Res.str().tt_p_preset_slots()
    }

    override fun length(): Int {
        return MAX - MIN + 2
    }

    override fun getValueAsString(index: Int): String {
        var s = getValue(index).toString()
        if (index == 0) s = toDefaultString(s)
        return s
    }

    companion object {
        private val KEY = SolidPresetCount::class.java.simpleName
        const val MAX = 15
        private const val MIN = 3
        const val DEFAULT = 5
    }
}
