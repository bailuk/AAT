package ch.bailu.aat_gtk.solid

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import java.io.File
import java.util.*
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences

class GtkStorage : StorageInterface {
    companion object {
        private val NODE = Preferences.userRoot().node(Strings.appPreferencesNode)
        private val OBSERVERS = ArrayList<OnPreferencesChanged>()

        fun save() {
            try {
                NODE.sync()
                NODE.flush()
            } catch (e: BackingStoreException) {
                AppLog.e(e)
            }
        }
    }

    override fun backup() {}
    override fun getSharedPrefsDirectory(): File {
        return File(NODE.absolutePath())
    }

    override fun restore() {}
    override fun readString(key: String): String {
        return NODE[key, ""].toString()
    }

    override fun writeString(key: String, value: String) {
        if (value != readString(key)) {
            NODE.put(key, value)
            propagate(key)
        }
    }

    override fun readInteger(key: String): Int {
        return NODE.getInt(key, 0)
    }

    override fun writeInteger(key: String, value: Int) {
        if (value != readInteger(key)) {
            NODE.putInt(key, value)
            propagate(key)
        }
    }

    override fun writeIntegerForce(key: String, value: Int) {
        writeInteger(key, value)
        propagate(key)
    }

    override fun readLong(key: String): Long {
        return NODE.getLong(key, 0)
    }

    override fun writeLong(key: String, value: Long) {
        if (value != readLong(key)) {
            NODE.putLong(key, value)
            propagate(key)
        }
    }

    override fun register(listener: OnPreferencesChanged) {
        if (!OBSERVERS.contains(listener)) {
            OBSERVERS.add(listener)
        }
    }

    override fun unregister(l: OnPreferencesChanged) {
        OBSERVERS.remove(l)
    }

    override fun isDefaultString(s: String): Boolean {
        return "" == s
    }

    override fun getDefaultString(): String {
        return ""
    }

    private fun propagate(key: String) {
        try {
            NODE.sync()
            for (l in OBSERVERS) {
                l.onPreferencesChanged(this, key)
            }
        } catch (e: BackingStoreException) {
            AppLog.e(this, e)
        }
    }
}
