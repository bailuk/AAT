package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;

@RequiresApi(api = 21)
public final class BleScannerSDK21 extends BleScanner {
    private final BluetoothAdapter adapter;

    private final ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            foundDevice(result.getDevice());
        }
    };


    private final ScanSettings settings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)               // also possible SCAN_MODE_BALANCED, SCAN_MODE_LOW_LATENCY
            .build();

    private final ScanFilter HrFilter = new ScanFilter.Builder()
            .setServiceUuid(ParcelUuid.fromString("0000180d-0000-1000-8000-00805f9b34fb"))
            .build();

    private final ScanFilter CscFilter = new ScanFilter.Builder()
            .setServiceUuid(ParcelUuid.fromString("00001816-0000-1000-8000-00805f9b34fb"))
            .build();

    private final ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>(Arrays.asList(HrFilter, CscFilter));


    protected BleScannerSDK21(BleSensorsSDK18 sensors) {
        super(sensors);
        adapter = sensors.getAdapter();
    }

    @Override
    public void start() {
        if (adapter != null) {
            BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
            if (scanner != null) {
                scanner.startScan(filters, settings, callback);
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
