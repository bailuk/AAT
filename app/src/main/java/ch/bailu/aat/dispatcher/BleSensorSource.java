package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;

public class BleSensorSource extends ContentSource {
    private final ServiceContext scontext;

    public BleSensorSource(ServiceContext sc) {
        scontext = sc;
    }


    private final BroadcastReceiver onSensorUpdated = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppLog.d(BleSensorSource.this, "onSensorUpdated()");
            sendUpdate(getIID(), getInfo());
        }

    };



    @Override
    public void requestUpdate() {
        sendUpdate(getIID(), getInfo());
    }

    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onSensorUpdated);
    }

    @Override
    public void onResume() {
        AppBroadcaster.register(scontext.getContext(), onSensorUpdated, AppBroadcaster.BLE_DEVICE_NOTIFIED);

    }

    @Override
    public int getIID() {
        return InfoID.HEART_RATE_SENSOR;
    }

    @Override
    public GpxInformation getInfo() {
        return scontext.getBleService().getInformation();
    }
}
