package ch.bailu.aat.services.tracker.location;

import android.content.Context;

public class InformationFilter extends LocationStackChainedItem {

    public InformationFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void newLocation(LocationInformation location) {
        if (location.hasAltitude() && location.hasBearing() && location.hasSpeed()) {
            sendLocation(location);
        }
    }

    @Override
    public void preferencesChanged(Context c, int i) {}
    
    

}
