package ch.bailu.aat.services.tracker.location;
import android.content.Context;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;

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
