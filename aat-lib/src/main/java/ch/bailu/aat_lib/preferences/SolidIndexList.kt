package ch.bailu.aat_lib.preferences

import javax.annotation.Nonnull

abstract class SolidIndexList(storage: StorageInterface, key: String) : AbsSolidType() {
    private val sindex: SolidInteger

    init {
        sindex = SolidInteger(storage, key)
    }

    abstract fun length(): Int
    protected abstract fun getValueAsString(index: Int): String
    @Nonnull
    override fun getValueAsString(): String {
        return getValueAsString(index)
    }

    override fun setValueFromString(string: String) {}
    protected fun validate(index: Int): Int {
        var result = index
        if (index < 0) result = length() - 1 else if (index >= length()) result = 0
        return result
    }

    open fun getStringArray(): Array<String> {
        val result = ArrayList<String>(length())
        for (i in 0 until length()) {
            result.add(getValueAsString(i))
        }
        return result.toTypedArray()
    }

    var index: Int
        get() = validate(sindex.getValue())
        set(i) {
            sindex.setValue(validate(i))
        }

    override fun getKey(): String {
        return sindex.getKey()
    }

    override fun getStorage(): StorageInterface {
        return sindex.getStorage()
    }

    fun cycle() {
        index += 1
    }
}
