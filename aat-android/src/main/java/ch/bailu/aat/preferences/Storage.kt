package ch.bailu.aat.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import ch.bailu.aat.util.ContextWrapperInterface
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface

class Storage (private val context: Context): ContextWrapperInterface, StorageInterface {

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

    override fun writeInteger(key: String, v: Int) {
        if (readInteger(key) != v) {
            editor.putInt(key, v)
            editor.apply()
        }
    }

    override fun writeIntegerForce(key: String, v: Int) {
        editor.remove(key)
        editor.apply()
        editor.putInt(key, v)
        editor.apply()
    }

    override fun readLong(key: String): Long {
        return preferences.getLong(key, 0)
    }

    override fun writeLong(key: String, v: Long) {
        if (readLong(key) != v) {
            editor.putLong(key, v)
            editor.apply()
        }
    }

    private val observers: MutableMap<OnPreferencesChanged, OnSharedPreferenceChangeListener> =
        HashMap(20)

    override fun register(observer: OnPreferencesChanged) {
        if (!observers.containsKey(observer)) {
            val listener =
                OnSharedPreferenceChangeListener { _: SharedPreferences?, key: String ->
                    observer.onPreferencesChanged(this@Storage, key)
                }
            preferences.registerOnSharedPreferenceChangeListener(listener)
            observers[observer] = listener
        } else {
            AppLog.e(this, "Observer was already registered")
        }
    }

    override fun unregister(observer: OnPreferencesChanged) {
        val listener = observers.remove(observer)
        if (listener != null) {
            preferences.unregisterOnSharedPreferenceChangeListener(listener)
        } else {
            AppLog.e(this, "Observer was not registered")
        }
    }

    override fun isDefaultString(s: String): Boolean {
        return defaultString == s
    }

    override fun getDefaultString(): String {
        return DEF_VALUE
    }

    override fun getContext(): Context {
        return context
    }

    companion object {
        private const val DEF_VALUE = "0"
        private const val GLOBAL_NAME = "Preferences"
    }
}
