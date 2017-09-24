package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;

public class CleanLocation extends LocationStackItem {
    private static final long LOCATION_LIFETIME_MILLIS=3*1000;

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
    public void passState(int state) {}


    @Override
    public void passLocation(LocationInformation location) {
        currentLocation = location;
    }


    @Override
    public void preferencesChanged(Context c, int i) {}

}
