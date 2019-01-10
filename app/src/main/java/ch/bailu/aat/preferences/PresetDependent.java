package ch.bailu.aat.preferences;

import android.content.Context;

public interface PresetDependent {
    void preferencesChanged(Context c, String key, int presetIndex);
}
