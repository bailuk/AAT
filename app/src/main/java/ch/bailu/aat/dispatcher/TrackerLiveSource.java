package ch.bailu.aat.dispatcher;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.services.ServiceContext;

public class TrackerLiveSource extends ContentSource {
    private static final int INTERVAL=1000;

    private final ServiceContext scontext;
    private final Timer timer;

    public TrackerLiveSource (ServiceContext sc) {
        timer = new Timer(new Runnable() {
            @Override
            public void run() {
                requestUpdate();
            }
        }, INTERVAL);
        scontext = sc;
    }


     private final BroadcastReceiver onTrackChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            requestUpdate();
        }

    };



    @Override
    public void requestUpdate() {
        sendUpdate(scontext.getTrackerService().getLoggerInformation());
        timer.kick();
    }


    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onTrackChanged);
        timer.close();

    }


    @Override
    public void onResume() {
        AppBroadcaster.register(scontext.getContext(), onTrackChanged, AppBroadcaster.TRACKER);
    }



}
