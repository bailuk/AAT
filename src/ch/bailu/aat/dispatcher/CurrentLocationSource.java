package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;

public class CurrentLocationSource extends ContentSource {
    private final ServiceContext scontext;

    private BroadcastReceiver onLocationChange = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGpxContent(scontext.getTrackerService().getLocation());
        }

    };



    public CurrentLocationSource(ServiceContext sc) {
        scontext=sc;
    }

    @Override
    public void close() {}



    @Override
    public void forceUpdate() {
        updateGpxContent(scontext.getTrackerService().getLocation());
    }

    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onLocationChange);
    }

    @Override
    public void onResume() {
        AppBroadcaster.register(scontext.getContext(), onLocationChange, AppBroadcaster.LOCATION_CHANGED);
    }
}
