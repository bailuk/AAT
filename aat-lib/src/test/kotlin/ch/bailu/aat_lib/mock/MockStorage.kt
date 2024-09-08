package ch.bailu.aat_lib.mock

import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface

class MockStorage : StorageInterface {
    var mockIntValue: Int = 0
    var mockStringValue = ""

    private val observers = ArrayList<OnPreferencesChanged>()

    override fun readString(key: String): String {
        return mockStringValue
    }

    override fun writeString(key: String, value: String) {
        if (mockStringValue != value) {
            mockStringValue = value
            notifyObservers(this, key)
        }
    }

    override fun readInteger(key: String): Int {
        return mockIntValue
    }

    override fun writeInteger(key: String, value: Int) {
        if (mockIntValue != value) {
            mockIntValue = value
            notifyObservers(this, key)
        }
    }

    private fun notifyObservers(storage: StorageInterface, key: String) {
        observers.forEach { it.onPreferencesChanged(storage, key) }
    }

    override fun writeIntegerForce(key: String, value: Int) {
        mockIntValue = value
        notifyObservers(this, key)
    }

    override fun readLong(key: String): Long {
        TODO("Not yet implemented")
    }

    override fun writeLong(key: String, value: Long) {
        TODO("Not yet implemented")
    }

    override fun register(onPreferencesChanged: OnPreferencesChanged) {
        observers.add(onPreferencesChanged)
    }

    override fun unregister(onPreferencesChanged: OnPreferencesChanged) {
        observers.remove(onPreferencesChanged)
    }

    override fun isDefaultString(s: String): Boolean {
        return s.isEmpty()
    }

    override fun getDefaultString(): String {
        return ""
    }
}
