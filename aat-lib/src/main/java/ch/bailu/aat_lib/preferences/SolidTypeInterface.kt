package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.description.ContentInterface
import ch.bailu.aat_lib.util.ui.ToolTipProvider

interface SolidTypeInterface : ContentInterface, ToolTipProvider {
    fun getKey(): String
    fun getStorage(): StorageInterface
    fun hasKey(key: String): Boolean
    fun register(listener: OnPreferencesChanged)
    fun unregister(listener: OnPreferencesChanged)
}
