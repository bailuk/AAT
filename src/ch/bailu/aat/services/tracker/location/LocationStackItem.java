package ch.bailu.aat.services.tracker.location;

import ch.bailu.aat.helpers.CleanUp;
import ch.bailu.aat.preferences.PresetDependent;

public abstract class LocationStackItem implements CleanUp, PresetDependent{
    public abstract void sendState(int state);
    public abstract void newState(int state);
    
    public abstract void sendLocation(LocationInformation location);
    public abstract void newLocation(LocationInformation location);
    
    
	public void appendStatusText(StringBuilder builder) {
		builder.append("<b>");
		builder.append(getClass().getSimpleName());
		builder.append("</b><br>");
	}
}
