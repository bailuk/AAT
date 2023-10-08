package ch.bailu.aat_lib.preferences

fun interface OnPreferencesChanged {
    fun onPreferencesChanged(storage: StorageInterface, key: String)
}
