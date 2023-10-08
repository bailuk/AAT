package ch.bailu.aat_lib.preferences

interface StorageInterface {
    fun readString(key: String): String
    fun writeString(key: String, value: String)
    fun readInteger(key: String): Int
    fun writeInteger(key: String, value: Int)
    fun writeIntegerForce(key: String, value: Int)
    fun readLong(key: String): Long
    fun writeLong(key: String, value: Long)
    fun register(onPreferencesChanged: OnPreferencesChanged)
    fun unregister(onPreferencesChanged: OnPreferencesChanged)
    fun isDefaultString(s: String): Boolean
    fun getDefaultString(): String
}
