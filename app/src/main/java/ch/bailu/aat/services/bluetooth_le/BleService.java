package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.util.AppBroadcaster;

public class BleService extends VirtualService {
    private final BleDevices devices;


    public BleService(ServiceContext sc) {
        super(sc);

        devices = BleDevices.factory(sc);


        AppBroadcaster.register(getContext(),
                onBluetoothStateChanged, BluetoothAdapter.ACTION_STATE_CHANGED);

    }


    BroadcastReceiver onBluetoothStateChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_OFF) {
                devices.scann();
            }
        }
    };


    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append(toString());
    }


    @Override
    public synchronized void close() {
        devices.close();
        getContext().unregisterReceiver(onBluetoothStateChanged);
    }


    public  synchronized void scan() {
        devices.scann();
    }


    @Override
    public synchronized String toString() {
        return devices.toString();
    }

    public synchronized GpxInformation getInformation(int iid) {
        return devices.getInformation(iid);
    }
}
