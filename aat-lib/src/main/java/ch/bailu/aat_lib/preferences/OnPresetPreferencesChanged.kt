package ch.bailu.aat_lib.preferences

fun interface OnPresetPreferencesChanged {
    fun onPreferencesChanged(storage: StorageInterface, key: String, presetIndex: Int)
}
