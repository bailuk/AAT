package ch.bailu.aat_lib.service.location;

import java.io.Closeable;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.OnPresetPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;

public abstract class LocationStackItem implements Closeable, OnPresetPreferencesChanged {
    public abstract void passState(int state);
    public abstract void passLocation(@Nonnull LocationInformation location);


    @Override
    public void close(){}


    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex) {}

    public void appendStatusText(StringBuilder builder) {
        builder.append("<b>");
        builder.append(getClass().getSimpleName());
        builder.append("</b><br>");
    }
}
