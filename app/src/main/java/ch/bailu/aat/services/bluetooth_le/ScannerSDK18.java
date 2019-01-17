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

import java.util.ArrayList;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.util.Objects;

@RequiresApi(api = 18)
public class ScannerSDK18 extends Scanner {

    private final static long SCAN_DURATION = 5000;
    private final Context context;

    private final BluetoothAdapter adapter;

    private final long startTimeMillis = System.currentTimeMillis();
    private ArrayList<Device> scanned = new ArrayList<>(5);

    private boolean isScanning = false;

    public ScannerSDK18(Context c) {
        context = c;
        adapter = getAdapter();

        scann();
    }


    @Override
    public void scann() {
        if (isScanning == false) {
            removeInvalidDevices();
            scanDevices();
        }
    }


    private void removeInvalidDevices() {
        for (int i = scanned.size()-1; i > -1; i--) {
            if (scanned.get(i).isValid() == false) {
                scanned.remove(i);
            }
        }
    }

    private BluetoothAdapter getAdapter() {
        BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        return bm.getAdapter();
    }


    private boolean isEnabled() {
        return adapter instanceof  BluetoothAdapter && adapter.isEnabled();
    }


    private void scanDevices() {
        if (isEnabled()) {
            isScanning = true;

            if (Build.VERSION.SDK_INT >= 21) {
                AppLog.d(this, "scanDevices21()");
                scanDevicesSDK21();
            } else {
                AppLog.d(this, "scanDevices18()");
                scanDevicesSDK18();
            }
        }
    }


    @RequiresApi(api = 21)
    private void scanDevicesSDK21() {
        BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();

        scanner.startScan( new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                if (!isScanned(result.getDevice())) {
                    scannDevice(result.getDevice());
                }

                if (timeout()) {
                    AppLog.d(ScannerSDK18.this, "stopScanSDK21()");
                    scanner.stopScan(this);
                    isScanning = false;
                }
            }
        });

    }



    private void scanDevicesSDK18() {
        adapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

                if (!isScanned(device)) {
                    scannDevice(device);
                }


                if (timeout()) {
                    AppLog.d(ScannerSDK18.this, "stopScanSDK18()");
                    adapter.stopLeScan(this);
                    isScanning = false;
                }
            }

        });
    }


    private boolean timeout() {
        return System.currentTimeMillis() - startTimeMillis > SCAN_DURATION;
    }



    private boolean isScanned(BluetoothDevice device) {
        for (Device d : scanned) {
            if (Objects.equals(d.getAddress(), device.getAddress())) return true;
        }
        return false;
    }


    private void scannDevice(BluetoothDevice device) {
        Device d = new Device(device);
        scanned.add(d);
        device.connectGatt(context, true, d);
    }


    @Override
    public String toString() {
        String s = "";
        String nl = "";

        for (Device d : scanned) {
            if (d.isValid()) {
                s = s + nl + d.toString();
                nl = "\n";
            }
        }
        return s;
    }
}
