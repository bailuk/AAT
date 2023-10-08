package ch.bailu.aat_lib.preferences;

public interface OnPresetPreferencesChanged {
    void onPreferencesChanged(StorageInterface storage, String key, int presetIndex);
}
