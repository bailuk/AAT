package ch.bailu.aat.services.tracker.location;

import android.content.Context;
import android.location.LocationManager;

public class SystemLocation extends RealLocation {
    private static final int GPS_INTERVAL=1000;
    private static final int NETWORK_INTERVAL=60*GPS_INTERVAL;

    private NetworkLocation networkLocation;

    private class NetworkLocation extends RealLocation {
        public NetworkLocation(LocationStackItem i, Context c) {
            super(i, c, LocationManager.NETWORK_PROVIDER, NETWORK_INTERVAL);
        }

        @Override
        public void sendState(int s) {}
    }

    public SystemLocation(LocationStackItem i, Context c) {
        super(i, c, LocationManager.GPS_PROVIDER, GPS_INTERVAL);
        enableNetworkLocation();
    }

    public SystemLocation(LocationStackItem i, Context c, int gpsInterval) {
        super(i, c, LocationManager.GPS_PROVIDER, gpsInterval);
        enableNetworkLocation();
    }


    @Override
    public void close() {
        super.close();
        disableNetworkLocation();
    }
    @Override
    public void sendState(int s) {
        if (s==STATE_ON) disableNetworkLocation();
        else enableNetworkLocation();
        super.sendState(s);
    }

    private void enableNetworkLocation() {
        if (networkLocation == null && getContext() != null) 
            networkLocation=new NetworkLocation(this, getContext());
    }

    private void disableNetworkLocation() {
        if (networkLocation != null) {
            networkLocation.close();
            networkLocation = null;
        }
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);

        if (networkLocation == null) {
            builder.append(NetworkLocation.class.getSimpleName());
            builder.append(": disabled<br>");
        } else
            networkLocation.appendStatusText(builder);
    }

}
