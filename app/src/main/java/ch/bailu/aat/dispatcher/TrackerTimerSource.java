package ch.bailu.aat.dispatcher;


import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.services.ServiceContext;

public class TrackerTimerSource extends ContentSource {
    private static final int INTERVAL=500;

    private final ServiceContext scontext;
    private final Timer timer;

    public TrackerTimerSource(ServiceContext sc) {
        timer = new Timer(new Runnable() {
            @Override
            public void run() {
                requestUpdate();
            }
        }, INTERVAL);

        scontext = sc;
    }

/*
     private final BroadcastReceiver onTrackChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            requestUpdate();
        }

    };
*/


    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.TRACKER_TIMER,
                scontext.getTrackerService().getLoggerInformation());
        timer.kick();
    }


    @Override
    public void onPause() {
  //      scontext.getContext().unregisterReceiver(onTrackChanged);
        timer.cancel();

    }


    @Override
    public void onResume() {
    //    AppBroadcaster.register(scontext.getContext(), onTrackChanged, AppBroadcaster.TRACKER);
        timer.kick();
    }
}
