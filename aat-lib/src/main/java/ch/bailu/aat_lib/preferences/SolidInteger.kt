package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.resources.Res

open class SolidInteger(private val storage: StorageInterface, private val key: String) : AbsSolidType() {
    open fun getValue(): Int {
        return getStorage().readInteger(getKey())
    }

    open fun setValue(value: Int) {
        getStorage().writeInteger(getKey(), value)
    }

    override fun getKey(): String {
        return key
    }

    override fun getStorage(): StorageInterface {
        return storage
    }

    override fun getValueAsString(): String {
        return getValue().toString()
    }

    @Throws(ValidationException::class)
    override fun setValueFromString(string: String) {
        var s = string
        s = s.trim { it <= ' ' }
        if (!validate(s)) {
            throw ValidationException(String.format(Res.str().error_integer(), s))
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
        // only positive/negative Integers, any size allowed
        private val VALIDATE = Regex("-?[1-9]\\d*")
    }
}
