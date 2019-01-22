package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.support.annotation.RequiresApi;

@RequiresApi(api = 21)
public abstract class BleScannerSDK21 extends BleScanner {
    private final BluetoothAdapter adapter;

    private final ScanCallback callback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            foundDevice(result.getDevice());
        }
    };


    protected BleScannerSDK21(BluetoothAdapter a) {
        adapter = a;
    }

    @Override
    public void start() {
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (scanner != null) { /*
            final ArrayList<ScanFilter> filters = new ArrayList(2);
            filters.add(new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(HeartRateServiceID.HEART_RATE_SERVICE))
                    .build());

            filters.add(new ScanFilter.Builder()
                    .setServiceUuid(new ParcelUuid(CscServiceID.CSC_SERVICE))
                    .build());

            scanner.startScan(filters, new ScanSettings.Builder().build(), callback);
            */
            scanner.startScan(callback);
        }
    }

    @Override
    public void stop() {
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
        if (scanner != null)
            scanner.stopScan(callback);
    }
}
