package ch.bailu.aat_lib.preferences.presets

import ch.bailu.aat_lib.description.FormatDisplay.Companion.f
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res


class SolidAccuracyFilter(storage: StorageInterface, index: Int) : SolidIndexList(storage, KEY + index) {
    private val sunit: SolidUnit = SolidUnit(storage)

    val minAccuracy: Float
        get() = VALUE_LIST[index]


    override fun getLabel(): String {
        return Res.str().p_accuracy_filter()
    }

    override fun length(): Int {
        return VALUE_LIST.size
    }

    public override fun getValueAsString(index: Int): String {
        return if (index == 0) Res.str().off() else (f().decimal2.format(
            (VALUE_LIST[index] * sunit.altitudeFactor).toDouble()
        )
                + sunit.altitudeUnit)
    }

    override fun getToolTip(): String? {
        return Res.str().tt_p_accuracy_filter()
    }

    companion object {
        private const val KEY = "accuracy_filter_"
        private val VALUE_LIST = floatArrayOf(
            999f,
            1f,
            2f,
            3f,
            4f,
            5f,
            10f,
            15f,
            20f,
            25f,
            30f,
            40f,
            50f,
            100f,
            200f
        )
    }
}
