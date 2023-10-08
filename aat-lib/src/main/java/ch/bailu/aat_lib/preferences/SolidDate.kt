package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.description.FF.Companion.f
import javax.annotation.Nonnull

class SolidDate(storage: StorageInterface, key: String, private val label: String) : SolidLong(storage, key) {

    override fun getValue(): Long {
        return if (super.getValue() == 0L) {
            System.currentTimeMillis()
        } else super.getValue()
    }

    @Nonnull
    override fun getValueAsString(): String {
        return f().LOCAL_DATE.format(getValue())
    }

    @Nonnull
    override fun getLabel(): String {
        return label
    }
}
