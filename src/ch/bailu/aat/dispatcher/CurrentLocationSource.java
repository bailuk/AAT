package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.tracker.TrackerService;

public class CurrentLocationSource extends ContentSource {
    private TrackerService service;

    private BroadcastReceiver onLocationChange = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGpxContent(service.getLocation());
        }

    };



    public CurrentLocationSource(TrackerService s) {
        service = s;
        AppBroadcaster.register(service, onLocationChange, AppBroadcaster.LOCATION_CHANGED);

    }

    @Override
    public void cleanUp() {
        service.unregisterReceiver(onLocationChange);
    }



    @Override
    public void forceUpdate() {
        updateGpxContent(service.getLocation());
    }
}
