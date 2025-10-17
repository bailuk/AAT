package ch.bailu.aat_gtk.preferences

import ch.bailu.aat_gtk.config.Environment
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences

class GtkStorage : StorageInterface {
    companion object {
        private val node = run {
            System.setProperty("java.util.prefs.userRoot", Environment.configHome)
            Preferences.userRoot().node(Strings.appPreferencesNode)
        }

        private var observers: List<OnPreferencesChanged> = emptyList()

        fun save() {
            try {
                node.sync()
                node.flush()
            } catch (e: BackingStoreException) {
                AppLog.e(this, e)
            }
        }
    }

    override fun readString(key: String): String {
        return node[key, ""].toString()
    }

    override fun writeString(key: String, value: String) {
        if (value != readString(key)) {
            node.put(key, value)
            propagate(key)
        }
    }

    override fun readInteger(key: String): Int {
        return node.getInt(key, 0)
    }

    override fun writeInteger(key: String, value: Int) {
        if (value != readInteger(key)) {
            node.putInt(key, value)
            propagate(key)
        }
    }

    override fun writeIntegerForce(key: String, value: Int) {
        writeInteger(key, value)
        propagate(key)
    }

    override fun readLong(key: String): Long {
        return node.getLong(key, 0)
    }

    override fun writeLong(key: String, value: Long) {
        if (value != readLong(key)) {
            node.putLong(key, value)
            propagate(key)
        }
    }

    override fun register(onPreferencesChanged: OnPreferencesChanged) {
        if (!observers.contains(onPreferencesChanged)) {
            observers = observers + onPreferencesChanged
        }
    }

    override fun unregister(onPreferencesChanged: OnPreferencesChanged) {
        observers = observers - onPreferencesChanged
    }

    override fun isDefaultString(s: String): Boolean {
        return "" == s
    }

    override fun getDefaultString(): String {
        return ""
    }

    private fun propagate(key: String) {
        try {
            node.sync()
            // Detach immutable list to safely loop trough it
            val observers = observers
            observers.forEach { it.onPreferencesChanged(this, key) }
        } catch (e: BackingStoreException) {
            AppLog.e(this, e)
        }
    }
}
