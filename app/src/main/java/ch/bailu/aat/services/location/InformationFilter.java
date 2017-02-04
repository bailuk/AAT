package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.util.ui.AppLog;

public class InformationFilter extends LocationStackChainedItem {

    public InformationFilter(LocationStackItem n) {
        super(n);
    }

    @Override
    public void close() {}

    @Override
    public void newLocation(LocationInformation location) {
        AppLog.d(this, "newLocation()");
        if (location.hasAltitude() && location.hasBearing() && location.hasSpeed()) {
            AppLog.d(this, "-> send");
            sendLocation(location);
        }
    }

    @Override
    public void preferencesChanged(Context c, int i) {}
    
    

}
