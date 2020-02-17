package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.RequiresApi;

@RequiresApi(api = 18)
public final class BleScannerSDK18 extends BleScanner {

    private final BluetoothAdapter adapter;
    private final BluetoothAdapter.LeScanCallback callback =
            (device, rssi, scanRecord) -> foundDevice(device);


    public BleScannerSDK18(BleSensorsSDK18 sensors) {
        super(sensors);
        adapter = sensors.getAdapter();
    }

    @Override
    public void start() {
        if (adapter != null)
            adapter.startLeScan(callback);
    }

    @Override
    public void stop() {

        if (adapter != null)
            adapter.stopLeScan(callback);
    }

}
