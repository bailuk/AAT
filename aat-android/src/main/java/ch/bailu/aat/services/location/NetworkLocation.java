package ch.bailu.aat.services.location;

import android.content.Context;
import android.location.LocationManager;

public final class NetworkLocation extends RealLocation {

    public NetworkLocation(LocationStackItem i, Context c, int interval) {
        super(i, c, LocationManager.NETWORK_PROVIDER, interval);
    }
}
