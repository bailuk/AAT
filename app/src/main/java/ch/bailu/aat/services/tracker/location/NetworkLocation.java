package ch.bailu.aat.services.tracker.location;

import android.content.Context;
import android.location.LocationManager;

public class NetworkLocation extends RealLocation {

    public static final int NETWORK_INTERVAL=60*SystemLocation.GPS_INTERVAL;

    public NetworkLocation(LocationStackItem i, Context c) {
        super(i, c, LocationManager.NETWORK_PROVIDER);
        init(NETWORK_INTERVAL);
    }

    @Override
    public void sendState(int s) {}

}
