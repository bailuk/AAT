package ch.bailu.aat.services.location;

import android.content.Context;
import android.location.LocationManager;

public class GpsLocation extends RealLocation {
    public GpsLocation(LocationStackItem i, Context c, int interval) {
        super(i, c, LocationManager.GPS_PROVIDER, interval);
    }
}
