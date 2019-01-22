package ch.bailu.aat.services.sensor;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.util.AppBroadcaster;

public class SensorService extends VirtualService {
    private final Sensors bluetoothLE;
    private final Sensors internal;


    public SensorService(ServiceContext sc) {
        super(sc);

        bluetoothLE = Sensors.factoryBle(sc);
        internal = Sensors.factoryInternal(sc.getContext());


        AppBroadcaster.register(getContext(),
                onBluetoothStateChanged, BluetoothAdapter.ACTION_STATE_CHANGED);

        scan();

    }


    BroadcastReceiver onBluetoothStateChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_OFF) {
                bluetoothLE.scann();
            }
        }
    };


    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append(toString());
    }


    @Override
    public synchronized void close() {
        bluetoothLE.close();
        internal.close();
        getContext().unregisterReceiver(onBluetoothStateChanged);
    }


    public  synchronized void scan() {
        bluetoothLE.scann();
    }


    @Override
    public synchronized String toString() {
        return bluetoothLE.toString() + internal.toString();
    }

    public synchronized GpxInformation getInformation(int iid) {
        GpxInformation info = internal.getInformation(iid);
        if (info == null) info = bluetoothLE.getInformation(iid);

        return info;
    }
}
