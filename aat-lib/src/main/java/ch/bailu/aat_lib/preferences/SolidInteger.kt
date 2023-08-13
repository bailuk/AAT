package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.resources.Res
import javax.annotation.Nonnull

open class SolidInteger(
    @param:Nonnull private val storage: StorageInterface,
    @param:Nonnull private val key: String
) : AbsSolidType() {
    open var value: Int
        get() = getStorage().readInteger(getKey())
        set(v) {
            getStorage().writeInteger(getKey(), v)
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

    @Throws(ValidationException::class)
    override fun setValueFromString(string: String) {
        var s = string
        s = s.trim { it <= ' ' }
        if (!validate(s)) {
            throw ValidationException(String.format(Res.str().error_integer(), s))
        } else {
            try {
                value = s.toInt()
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
