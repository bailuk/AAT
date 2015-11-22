package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.overlay.OverlayService;


public class OverlaySource extends ContentSource {
    private final OverlayService service;

    private final BroadcastReceiver	 onOverlayChanged = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            update(AppBroadcaster.getExtraID(intent));


        }


    };


    public OverlaySource(OverlayService o) {
        service = o;

        AppBroadcaster.register(service, onOverlayChanged, AppBroadcaster.OVERLAY_CHANGED);
    }	





    @Override
    public void cleanUp() {
        service.unregisterReceiver(onOverlayChanged);
    }


    @Override
    public void forceUpdate() {
        for (int i=0; i<OverlayService.MAX_OVERLAYS; i++) 
            update(GpxInformation.ID.INFO_ID_OVERLAY+i);
    }

    private void update(int id) {
        updateGpxContent(service.getInformation(id));
    }

}
