package ch.bailu.aat.services.tracker.location;

import java.io.Closeable;

import ch.bailu.aat.preferences.PresetDependent;

public abstract class LocationStackItem implements Closeable, PresetDependent{
    public abstract void sendState(int state);
    public abstract void newState(int state);

    public abstract void sendLocation(LocationInformation location);
    public abstract void newLocation(LocationInformation location);


    @Override
    public void close(){}

    public void appendStatusText(StringBuilder builder) {
        builder.append("<b>");
        builder.append(getClass().getSimpleName());
        builder.append("</b><br>");
    }
}
