package ch.bailu.aat.services.location;

import android.content.Context;

import java.io.Closeable;

import ch.bailu.aat.preferences.PresetDependent;

public abstract class LocationStackItem implements Closeable, PresetDependent{
    public abstract void passState(int state);
    public abstract void passLocation(LocationInformation location);


    @Override
    public void close(){}


    @Override
    public void preferencesChanged(Context c, int i) {}


    public void appendStatusText(StringBuilder builder) {
        builder.append("<b>");
        builder.append(getClass().getSimpleName());
        builder.append("</b><br>");
    }
}
