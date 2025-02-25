package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.description.FF.Companion.f

class SolidDate(storage: StorageInterface, key: String, private val label: String) : SolidLong(storage, key) {

    override fun getValue(): Long {
        return if (super.getValue() == 0L) {
            System.currentTimeMillis()
        } else super.getValue()
    }


    override fun getValueAsString(): String {
        return f().localDate.format(getValue())
    }


    override fun getLabel(): String {
        return label
    }
}
