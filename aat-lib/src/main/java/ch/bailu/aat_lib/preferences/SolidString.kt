package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.exception.ValidationException

open class SolidString(private val storage: StorageInterface, private val key: String) : AbsSolidType() {

    override fun getValueAsString(): String {
        return getStorage().readString(getKey())
    }

    @Throws(ValidationException::class)
    override fun setValueFromString(string: String) {
        setValue(string)
    }

    open fun setValue(v: String) {
        getStorage().writeString(key, v)
    }

    override fun getKey(): String {
        return key
    }

    override fun getStorage(): StorageInterface {
        return storage
    }

    /**
     * List of possible values to select a value from
     * This is used by selection lists in the settings dialog
     */
    open fun buildSelection(list: ArrayList<String> = ArrayList(5)): ArrayList<String> {
        return list
    }

    fun getValueAsStringNonDef(): String {
        val s = getValueAsString()
        return if (storage.isDefaultString(s)) "" else s
    }

    fun getDefaultValueFromSelection(fallBack: String = getStorage().getDefaultString()): String {
        val list = buildSelection()
        list.add(fallBack)
        return list[0]
    }

    fun setDefaultValue(): String {
        val r = getDefaultValueFromSelection()
        setValue(r)
        return r
    }
}
