package ch.bailu.aat_lib.preferences.general

import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.preferences.SolidInteger
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res

class SolidWeight(s: StorageInterface) : SolidInteger(s, KEY) {
    override fun getLabel(): String {
        return Res.str().p_weight_title()
    }

    fun setDefaults() {
        setValue(75)
    }

    @Throws(ValidationException::class)
    override fun setValueFromString(string: String) {
        var s = string
        s = s.trim { it <= ' ' }
        if (!validate(s)) {
            throw ValidationException(Res.str().error_integer_positive())
        } else {
            try {
                setValue(s.toInt())
            } catch (e: NumberFormatException) {
                e(this, e)
            }
        }
    }

    override fun validate(s: String): Boolean {
        return s.matches(VALIDATE)
    }

    companion object {
        private const val KEY = "weight"

        // only positive Integers, from 1-999 allowed
        private val VALIDATE = Regex("[1-9]\\d{0,2}")
    }
}
