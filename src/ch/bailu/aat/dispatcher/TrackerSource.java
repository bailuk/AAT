package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;

public class TrackerSource extends ContentSource {

    private ServiceContext scontext;

    public TrackerSource (ServiceContext sc) {
        scontext = sc;

    }

    private BroadcastReceiver onStateChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGpxContent(scontext.getTrackerService().getTrackerInformation());		}

    };

    private BroadcastReceiver onTrackChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGpxContent(scontext.getTrackerService().getTrackerInformation());
        }

    };


    @Override
    public void forceUpdate() {
        updateGpxContent(scontext.getTrackerService().getTrackerInformation());
    }

    
    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onStateChanged);
        scontext.getContext().unregisterReceiver(onTrackChanged);
    }

    
    @Override
    public void onResume() {
        AppBroadcaster.register(scontext.getContext(), onStateChanged, AppBroadcaster.TRACKER_STATE);
        AppBroadcaster.register(scontext.getContext(), onTrackChanged, AppBroadcaster.TRACKER);
    }

    
    @Override
    public void close() {}
    
}
