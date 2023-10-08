package ch.bailu.aat.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface

class Storage (private val context: Context): StorageInterface {

    private val preferences: SharedPreferences = context.getSharedPreferences(GLOBAL_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    override fun readString(key: String): String {
        return preferences.getString(key, DEF_VALUE)!!
    }

    override fun writeString(key: String, value: String) {
        if (readString(key) != value) {
            editor.putString(key, value)
            editor.apply()
        }
    }

    override fun readInteger(key: String): Int {
        return try {
            preferences.getInt(key, 0)
        } catch (e: ClassCastException) {
            0
        }
    }

    override fun writeInteger(key: String, value: Int) {
        if (readInteger(key) != value) {
            editor.putInt(key, value)
            editor.apply()
        }
    }

    override fun writeIntegerForce(key: String, value: Int) {
        editor.remove(key)
        editor.apply()
        editor.putInt(key, value)
        editor.apply()
    }

    override fun readLong(key: String): Long {
        return preferences.getLong(key, 0)
    }

    override fun writeLong(key: String, value: Long) {
        if (readLong(key) != value) {
            editor.putLong(key, value)
            editor.apply()
        }
    }

    private val observers: MutableMap<OnPreferencesChanged, OnSharedPreferenceChangeListener> =
        HashMap(20)

    override fun register(onPreferencesChanged: OnPreferencesChanged) {
        if (!observers.containsKey(onPreferencesChanged)) {
            val newListener =
                OnSharedPreferenceChangeListener { _: SharedPreferences?, key: String ->
                    onPreferencesChanged.onPreferencesChanged(this@Storage, key)
                }
            preferences.registerOnSharedPreferenceChangeListener(newListener)
            observers[onPreferencesChanged] = newListener
        } else {
            AppLog.e(this, "Observer was already registered")
        }
    }

    override fun unregister(onPreferencesChanged: OnPreferencesChanged) {
        val listener = observers.remove(onPreferencesChanged)
        if (listener != null) {
            preferences.unregisterOnSharedPreferenceChangeListener(listener)
        } else {
            AppLog.e(this, "Observer was not registered")
        }
    }

    override fun isDefaultString(s: String): Boolean {
        return getDefaultString() == s
    }

    override fun getDefaultString(): String {
        return DEF_VALUE
    }

    fun getContext(): Context {
        return context
    }

    companion object {
        private const val DEF_VALUE = "0"
        private const val GLOBAL_NAME = "Preferences"
    }
}
