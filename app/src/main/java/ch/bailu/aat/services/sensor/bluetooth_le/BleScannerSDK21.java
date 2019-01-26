package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.support.annotation.RequiresApi;

@RequiresApi(api = 21)
public class BleScannerSDK21 extends BleScanner {
    private final BluetoothAdapter adapter;

    private final ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            foundDevice(result.getDevice());
        }
    };


    protected BleScannerSDK21(BleSensorsSDK18 sensors) {
        super(sensors);
        adapter = sensors.getAdapter();
    }

    @Override
    public void start() {
        if (adapter != null) {
            BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
            if (scanner != null) {
                scanner.startScan(callback);
            }
        }
    }

    @Override
    public void stop() {
        if (adapter != null) {
            BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
            if (scanner != null)
                scanner.stopScan(callback);
        }
    }
}
