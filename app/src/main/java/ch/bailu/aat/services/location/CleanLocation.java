package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.util.ui.AppLog;

public class CleanLocation extends LocationStackItem {
    private static final long LOCATION_LIFETIME_MILLIS=5*1000;

    private GpxPointInterface currentLocation=GpxPoint.NULL;

    public boolean hasLoggableLocation() {
        return (System.currentTimeMillis() - currentLocation.getTimeStamp()) < LOCATION_LIFETIME_MILLIS;
    }

    public GpxPointInterface getCleanLocation() {
        GpxPointInterface location=currentLocation;
        currentLocation=GpxPoint.NULL;

        return location;
    }


    @Override
    public void newLocation(LocationInformation location) {
//        AppLog.d(this, "newLocation()");
//        if (hasLoggableLocation()) AppLog.d(this, "hsLoggableLocation()");
        currentLocation=location;
    }

    @Override
    public void sendLocation(LocationInformation location) {}

    @Override
    public void sendState(int state) {}

    @Override
    public void newState(int state) {}

    @Override
    public void close() {}

    @Override
    public void preferencesChanged(Context c, int i) {}

}
