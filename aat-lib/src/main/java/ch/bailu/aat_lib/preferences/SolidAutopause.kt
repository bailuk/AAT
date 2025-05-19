package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.description.FF.Companion.f
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res

abstract class SolidAutopause protected constructor(
    storage: StorageInterface, key: String, preset: Int) : SolidIndexList(storage, key + preset
) {
    private val sunit: SolidUnit = SolidUnit(storage)

    val triggerSpeed: Float
        get() = SPEED_VALUES[index]
    val triggerLevel: Int
        get() = TRIGGER_VALUES[index]
    val triggerLevelMillis: Int
        get() = TRIGGER_VALUES[index] * 1000
    val isEnabled: Boolean
        get() = index > 0

    override fun length(): Int {
        return SPEED_VALUES.size
    }

    public override fun getValueAsString(index: Int): String {
        return if (index == 0) Res.str().off() else "< " +
                f().N2.format(
                    (SPEED_VALUES[index] * sunit.speedFactor).toDouble()
                ) +
                sunit.speedUnit + " - " +
                TRIGGER_VALUES[index] + "s"
    }

    companion object {
        private val SPEED_VALUES = floatArrayOf(
            0f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f
        )
        private val TRIGGER_VALUES = intArrayOf(
            0,
            3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5,
            10, 10, 10, 10, 10, 10,
            20, 20, 20, 20, 20, 20
        )
    }
}
