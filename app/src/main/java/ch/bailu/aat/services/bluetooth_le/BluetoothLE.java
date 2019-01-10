package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import ch.bailu.aat.activities.ActivityContext;
import ch.bailu.aat.util.ui.AppLog;

public class BluetoothLE {

    private final ActivityContext context;

    private BluetoothAdapter adapter;


    public BluetoothLE(ActivityContext c) {
        context = c;

        if (enable()) {
            scanDevices();
            //disable();
        }
    }



    public boolean enable() {
        if (!isEnabled()) {
            getAdapter();
        }
        return isEnabled();
    }

    public void disable() {
        if (isEnabled()) {
            adapter.disable();
            adapter = null;
        }
    }


    private void getAdapter() {
        if (Build.VERSION.SDK_INT >= 18) {
            getAdapterSDK18();
        }
    }


    @RequiresApi(api = 18)
    private void getAdapterSDK18() {
        BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        adapter = bm.getAdapter();


    }


    public boolean isEnabled() {
        return adapter instanceof  BluetoothAdapter && adapter.isEnabled();
    }


    public boolean isSupported() {
        return Build.VERSION.SDK_INT >= 18;
    }


    private void scanDevices() {
        if (isEnabled()) {
            if (Build.VERSION.SDK_INT >= 21) {
                scanDevicesSDK21();
            } else if (Build.VERSION.SDK_INT >= 18) {
                scanDevicesSDK18();
            }
        }
    }


    @RequiresApi(api = 21)
    private void scanDevicesSDK21() {
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();

        scanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();
                logDevice(device);
            }
        });
    }


    @RequiresApi(api = 18)
    private void scanDevicesSDK18() {
        adapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                logDevice(device);
            }
        });
    }


    private void logDevice(BluetoothDevice device) {
        AppLog.d(this, device.getName());
    }
}
