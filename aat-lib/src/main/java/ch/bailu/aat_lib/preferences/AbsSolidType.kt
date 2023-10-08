package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.util.Objects.equals
import javax.annotation.Nonnull

abstract class AbsSolidType : SolidTypeInterface {
    open fun getIconResource(): String {
        return ""
    }

    @Nonnull
    override fun getLabel(): String {
        return NULL_LABEL
    }

    @Throws(ValidationException::class)
    abstract fun setValueFromString(string: String)
    override fun hasKey(key: String): Boolean {
        return equals(key, getKey())
    }

    override fun register(listener: OnPreferencesChanged) {
        getStorage().register(listener)
    }

    override fun unregister(listener: OnPreferencesChanged) {
        getStorage().unregister(listener)
    }

    @Nonnull
    override fun toString(): String {
        return getValueAsString()
    }

    override fun getToolTip(): String? {
        return null
    }

    open fun validate(s: String): Boolean {
        return true
    }

    companion object {
        const val NULL_LABEL = ""
        private const val DEFAULT_MARKER = " ✓"

        @JvmStatic
        protected fun toDefaultString(s: String): String {
            return s + DEFAULT_MARKER
        }

        @JvmStatic
        protected fun toDefaultString(s: String, sel: Int): String {
            return if (sel == 0) toDefaultString(s) else s
        }
    }
}
