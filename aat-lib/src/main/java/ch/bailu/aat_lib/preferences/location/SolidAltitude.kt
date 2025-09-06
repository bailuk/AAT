package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.preferences.SolidInteger
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.resources.Res
import kotlin.math.roundToInt

open class SolidAltitude(storage: StorageInterface, key: String, private val unit: Int) : SolidInteger(storage, key) {
    fun addUnit(s: String): String {
        return s + " [" + SolidUnit.ALT_UNIT[unit] + "]"
    }

    override fun getValueAsString(): String {
        return (getValue() * SolidUnit.ALT_FACTOR[unit]).roundToInt().toString()
    }

    override fun setValueFromString(string: String) {
        if (!validate(string)) {
            throw ValidationException(Res.str().error_integer())
        }

        try {
            setValue((string.toFloat() / SolidUnit.ALT_FACTOR[unit]).roundToInt())
        } catch (e: NumberFormatException) {
            e(this, e)
        }
    }
}
