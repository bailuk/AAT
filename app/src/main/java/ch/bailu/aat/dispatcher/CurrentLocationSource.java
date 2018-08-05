package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;

public class CurrentLocationSource extends ContentSource {
    private final ServiceContext scontext;

    private final BroadcastReceiver onLocationChange = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendUpdate(InfoID.LOCATION, scontext.getLocationService().getLocationInformation());
        }

    };



    public CurrentLocationSource(ServiceContext sc) {
        scontext=sc;
    }





    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.LOCATION, scontext.getLocationService().getLocationInformation());
    }


    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onLocationChange);
    }

    @Override
    public void onResume() {
        AppBroadcaster.register(scontext.getContext(), onLocationChange,
                AppBroadcaster.LOCATION_CHANGED);
    }

    @Override
    public int getIID() {
        return InfoID.LOCATION;
    }

    @Override
    public GpxInformation getInfo() {
        return scontext.getLocationService().getLocationInformation();
    }

}
