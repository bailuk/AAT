package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.tracker.TrackerService;

public class TrackerSource extends ContentSource {

    private TrackerService service;

    public TrackerSource (TrackerService s) {
        service = s;

        AppBroadcaster.register(service, onStateChanged, AppBroadcaster.TRACKER_STATE);
        AppBroadcaster.register(service, onTrackChanged, AppBroadcaster.TRACKER);
    }

    @Override
    public void close() {
        service.unregisterReceiver(onStateChanged);
        service.unregisterReceiver(onTrackChanged);
    }


    private BroadcastReceiver onStateChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGpxContent(service.getTrackerInformation());		}

    };

    private BroadcastReceiver onTrackChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGpxContent(service.getTrackerInformation());
        }

    };


    @Override
    public void forceUpdate() {
        updateGpxContent(service.getTrackerInformation());
    }

}
