package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.preferences.SolidInteger
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class SolidPressureAtSeaLevel(storage: StorageInterface) : SolidInteger(storage, KEY) {
    override fun getLabel(): String {
        return Res.str().p_pressure_sealevel()
    }

    var pressure: Float
        get() = getValue() / 100f
        set(pressure) {
            setValue((pressure * 100f).toInt())
        }

    override fun setValueFromString(string: String) {
        try {
            pressure = string.toFloat()
        } catch (e: NumberFormatException) {
            e(this, e)
        }
    }

    override fun getValueAsString(): String {
        return pressure.toString()
    }

    companion object {
        private const val KEY = "PressureAtSeaLevel"
    }
}
