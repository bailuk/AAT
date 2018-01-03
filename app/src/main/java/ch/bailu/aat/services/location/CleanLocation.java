package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.description.format.UTC;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.util.ui.AppLog;

public class CleanLocation extends LocationStackItem {
    private static final long LOCATION_LIFETIME_MILLIS=3*1000;

    private GpxPointInterface currentLocation=GpxPoint.NULL;
    private long creationTime=0;



    public boolean hasLoggableLocation() {
        return (System.currentTimeMillis() - creationTime) < LOCATION_LIFETIME_MILLIS;
    }


    public GpxPointInterface getCleanLocation() {
        GpxPointInterface location=currentLocation;
        currentLocation=GpxPoint.NULL;
        creationTime = 0;

        return location;
    }


    @Override
    public void passState(int state) {}


    @Override
    public void passLocation(LocationInformation location) {
        if (location.isFromGPS()) {
            currentLocation = location;
            creationTime = location.getCreationTime();
        }
    }


    @Override
    public void preferencesChanged(Context c, int i) {}

}
