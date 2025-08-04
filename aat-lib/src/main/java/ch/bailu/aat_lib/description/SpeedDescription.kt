package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit

abstract class SpeedDescription(storage: StorageInterface) : FloatDescription() {
    private val sunit: SolidUnit = SolidUnit(storage)

    override fun getUnit(): String {
        return sunit.speedUnit
    }

    override fun getValue(): String {
        val speedFactor = sunit.speedFactor
        val speed = cache* speedFactor
        return FormatDisplay.f().decimal1.format(speed.toDouble())
    }

    fun getSpeedDescription(value: Float): String {
        setCache(value)
        return getValue() + " " + getUnit()
    }
}
