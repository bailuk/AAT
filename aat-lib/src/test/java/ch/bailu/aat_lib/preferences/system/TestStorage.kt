package ch.bailu.aat_lib.preferences.system

import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface

class TestStorage : StorageInterface {
    var intVal: Int = 0

    override fun readString(key: String): String {
        TODO("Not yet implemented")
    }

    override fun writeString(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun readInteger(key: String): Int {
        return intVal
    }

    override fun writeInteger(key: String, value: Int) {
        intVal = value
    }

    override fun writeIntegerForce(key: String, value: Int) {
        TODO("Not yet implemented")
    }

    override fun readLong(key: String): Long {
        TODO("Not yet implemented")
    }

    override fun writeLong(key: String, value: Long) {
        TODO("Not yet implemented")
    }

    override fun register(onPreferencesChanged: OnPreferencesChanged) {
        TODO("Not yet implemented")
    }

    override fun unregister(onPreferencesChanged: OnPreferencesChanged) {
        TODO("Not yet implemented")
    }

    override fun isDefaultString(s: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDefaultString(): String {
        TODO("Not yet implemented")
    }
}
