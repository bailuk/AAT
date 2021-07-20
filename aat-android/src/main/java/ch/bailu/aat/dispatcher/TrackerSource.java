package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.ContentSource;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;

public class TrackerSource extends ContentSource {

    private final ServiceContext scontext;

    public TrackerSource (ServiceContext sc) {
        scontext = sc;

    }

    private final BroadcastReceiver onTrackChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendUpdate(InfoID.TRACKER, scontext.getTrackerService().getLoggerInformation());
        }

    };


    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.TRACKER, scontext.getTrackerService().getLoggerInformation());
    }


    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onTrackChanged);
    }


    @Override
    public void onResume() {
        OldAppBroadcaster.register(scontext.getContext(), onTrackChanged, AppBroadcaster.TRACKER);
    }

    @Override
    public int getIID() {
        return InfoID.TRACKER;
    }

    @Override
    public GpxInformation getInfo() {
        return scontext.getTrackerService().getLoggerInformation();
    }


}
