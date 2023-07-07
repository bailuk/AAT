package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.util.Objects

abstract class GpxAttributes {
    abstract operator fun get(keyIndex: Int): String
    abstract fun hasKey(keyIndex: Int): Boolean
    abstract fun size(): Int
    abstract fun getAt(index: Int): String
    abstract fun getKeyAt(index: Int): Int
    fun getSKeyAt(index: Int): String {
        return Keys.toString(getKeyAt(index))
    }

    open fun put(keyIndex: Int, value: String) {}
    open fun getAsFloat(keyIndex: Int): Float {
        return Objects.toFloat(get(keyIndex))
    }

    open fun getAsLong(keyIndex: Int): Long {
        return Objects.toLong(get(keyIndex))
    }

    open fun getAsInteger(keyIndex: Int): Int {
        return Objects.toInt(get(keyIndex))
    }

    open fun getAsBoolean(keyIndex: Int): Boolean {
        return Objects.toBoolean(get(keyIndex))
    }

    companion object {
        const val NULL_VALUE = ""
    }
}
