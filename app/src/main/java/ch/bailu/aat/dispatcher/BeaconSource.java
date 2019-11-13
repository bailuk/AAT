package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;

public final class BeaconSource extends ContentSource {
    private final ServiceContext scontext;

    public BeaconSource (ServiceContext sc) {
        scontext = sc;
    }

    private final BroadcastReceiver onTrackChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendUpdate(InfoID.BEACON, scontext.getBeaconService().getLoggerInformation());
        }
    };

    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.BEACON, scontext.getBeaconService().getLoggerInformation());
    }

    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onTrackChanged);
    }

    @Override
    public void onResume() {
        AppBroadcaster.register(scontext.getContext(), onTrackChanged, AppBroadcaster.BEACON);
    }

    @Override
    public int getIID() {
        return InfoID.BEACON;
    }

    @Override
    public GpxInformation getInfo() {
        return scontext.getBeaconService().getLoggerInformation();
    }
}
