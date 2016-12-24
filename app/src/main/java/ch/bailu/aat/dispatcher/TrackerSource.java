package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;

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
        AppBroadcaster.register(scontext.getContext(), onTrackChanged, AppBroadcaster.TRACKER);
    }

    

}
