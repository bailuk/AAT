package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;

public final class CleanLocation extends LocationStackItem {
    private static final long LOCATION_LIFETIME_MILLIS=3*1000;

    private GpxInformation loggableLocation = GpxInformation.NULL;
    private long creationTime = 0;


    public boolean hasLoggableLocation(GpxInformation lastLocation) {
        return (loggableLocation != lastLocation &&
                (System.currentTimeMillis() - creationTime) < LOCATION_LIFETIME_MILLIS);
    }

    public GpxInformation getLoggableLocation() {
        return loggableLocation;
    }


    @Override
    public void passState(int state) {}


    @Override
    public void passLocation(LocationInformation location) {
        if (location.isFromGPS()) {
            loggableLocation = location;
            creationTime = location.getCreationTime();
        }
    }


    @Override
    public void preferencesChanged(Context c, String key, int presetIndex) {}

}
