package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.resources.Res

open class SolidLong(private val storage: StorageInterface, private val key: String) :
    AbsSolidType() {
    open var value: Long
        get() = getStorage().readLong(getKey())
        set(v) {
            getStorage().writeLong(getKey(), v)
        }

    @Throws(ValidationException::class)
    override fun setValueFromString(string: String) {
        val s = string.trim { it <= ' ' }

        if (!validate(s)) {
            throw ValidationException(String.format(Res.str().error_long(), s))
        } else {
            try {
                value = s.toLong()
            } catch (e: NumberFormatException) {
                e(this, e)
            }
        }
    }

    override fun getKey(): String {
        return key
    }

    override fun getStorage(): StorageInterface {
        return storage
    }

    override fun getValueAsString(): String {
        return value.toString()
    }

    override fun validate(s: String): Boolean {
        // regex long, not 100% correct
        return s.matches(VALIDATE)
    }

    companion object {
        private val VALIDATE = Regex("^-?\\d{1,19}$")
    }
}
