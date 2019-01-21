package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.RequiresApi;

@RequiresApi(api = 18)
public abstract class BleScannerSDK18 extends BleScanner {

    private final BluetoothAdapter adapter;
    private final BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            foundDevice(device);
        }
    };


    public BleScannerSDK18(BluetoothAdapter a) {
        adapter = a;
    }

    @Override
    public void start() {
        adapter.startLeScan(callback);
    }

    @Override
    public void stop() {
        adapter.stopLeScan(callback);
    }

}
