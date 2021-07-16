package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.gpx.StateID;

public final class GpsOrNetworkLocation extends LocationStackChainedItem {

    private final NetworkLocation network;
    private final GpsLocation gps;


    private boolean haveGps=false;


    public GpsOrNetworkLocation(LocationStackItem i, Context c, int interval) {
        super(i);

        network = new NetworkLocation(new LocationStackItem() {

            @Override
            public void passState(int state) {}

            @Override
            public void passLocation(LocationInformation location) {
                if (!haveGps) GpsOrNetworkLocation.this.passLocation(location);
            }

        }, c, interval * 5);

        gps = new GpsLocation(new LocationStackItem() {
            @Override
            public void passState(int state) {
                haveGps =  (state == StateID.ON);
                GpsOrNetworkLocation.this.passState(state);
            }


            @Override
            public void passLocation(LocationInformation location) {
                GpsOrNetworkLocation.this.passLocation(location);
            }

        }, c, interval);

    }



    @Override
    public void close() {
        super.close();
        network.close();
        gps.close();
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);

        network.appendStatusText(builder);
        gps.appendStatusText(builder);

        if (haveGps) {
            builder.append("have GPS");
        }
    }
}
