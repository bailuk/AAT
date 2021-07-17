package ch.bailu.aat_lib.service.location;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.StorageInterface;

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
            AppLog.d(this, "location accepted");
            loggableLocation = location;
            creationTime = location.getCreationTime();
        } else {
            AppLog.d(this, "location not accepted");
        }
    }


    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex) {}

}

