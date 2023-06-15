package ch.bailu.aat.services.location;

import android.content.Context;
import android.location.LocationManager;

import ch.bailu.aat_lib.service.location.LocationStackItem;

public final class NetworkLocation extends RealLocation {

    public NetworkLocation(LocationStackItem i, Context c, int interval) {
        super(i, c, LocationManager.NETWORK_PROVIDER, interval);
    }
}
