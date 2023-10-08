package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.description.FF.Companion.f
import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res
import javax.annotation.Nonnull

class SolidDistanceFilter(storage: StorageInterface, i: Int) : SolidIndexList(storage, KEY + i) {
    private val sunit: SolidUnit

    init {
        sunit = SolidUnit(storage)
    }

    val minDistance: Float
        get() = VALUE_LIST[index]

    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_distance_filter()
    }

    override fun length(): Int {
        return VALUE_LIST.size
    }

    public override fun getValueAsString(index: Int): String {
        if (index == 0) return Res.str().off()
        if (index == length() - 1) Res.str().auto()
        return (f().N2.format((VALUE_LIST[index] * sunit.altitudeFactor).toDouble())
                + sunit.altitudeUnit)
    }

    override fun getToolTip(): String? {
        return Res.str().tt_p_distance_filter()
    }

    companion object {
        private const val KEY = "distance_filter_"
        private val VALUE_LIST = floatArrayOf(
            0f,
            1f,
            2f,
            4f,
            6f,
            8f,
            10f,
            15f,
            20f,
            25f,
            30f,
            99f
        )
    }
}
