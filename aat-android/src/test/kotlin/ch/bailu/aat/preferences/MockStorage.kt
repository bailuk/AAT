package ch.bailu.aat.preferences

import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface

class MockStorage : StorageInterface {
    var mockIntValue: Int = 0
    var mockStringValue = ""
    var mockLongValue: Long = 0L


    override fun readString(key: String): String {
        return mockStringValue
    }

    override fun writeString(key: String, value: String) {
        mockStringValue = value
    }

    override fun readInteger(key: String): Int {
        return mockIntValue
    }

    override fun writeInteger(key: String, value: Int) {
        mockIntValue = value
    }

    override fun writeIntegerForce(key: String, value: Int) {
        TODO("Not yet implemented")
    }

    override fun readLong(key: String): Long {
        return mockLongValue
    }

    override fun writeLong(key: String, value: Long) {
        mockLongValue = value
    }

    override fun register(onPreferencesChanged: OnPreferencesChanged) {
        TODO("Not yet implemented")
    }

    override fun unregister(onPreferencesChanged: OnPreferencesChanged) {
        TODO("Not yet implemented")
    }

    override fun isDefaultString(s: String): Boolean {
        return s.isEmpty()
    }

    override fun getDefaultString(): String {
        return ""
    }
}
