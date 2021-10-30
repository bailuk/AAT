package ch.bailu.aat_lib.preferences;

import javax.annotation.Nonnull;

public interface OnPreferencesChanged {
    void onPreferencesChanged(@Nonnull StorageInterface storage, @Nonnull String key);
}
