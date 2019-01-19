package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.ToDo;
import ch.bailu.util_java.util.Objects;

@RequiresApi(api = 18)
public class BleDevicesSDK18 extends BleDevices {

    private final static long SCAN_DURATION = 5000;

    private final Context context;
    private final BluetoothAdapter adapter;

    private ArrayList<Device> scanned = new ArrayList<>(5);

    private final BleScanner scanner;


    private final Timer timer = new Timer(new Runnable() {
        @Override
        public void run() {
            scanner.stop();
        }
    }, SCAN_DURATION);




    public BleDevicesSDK18(Context c) {
        context = c;
        adapter = getAdapter();
        scanner = BleScanner.factory(adapter, this);

        scann();
    }



    @Override
    public void scann() {
        if (isEnabled()) {
            timer.kick();
            scanner.stop();
            removeInvalidDevices();
            scanner.start();

        } else {
            timer.cancel();
            scanner.stop();
            scanned.clear();

        }

        AppBroadcaster.broadcast(context, AppBroadcaster.BLE_DEVICE_SCANNED);
    }


    private BluetoothAdapter getAdapter() {
        BluetoothManager bm = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        return bm.getAdapter();
    }


    private boolean isEnabled() {
        return adapter instanceof  BluetoothAdapter && adapter.isEnabled();
    }



    private void removeInvalidDevices() {
        for (int i = scanned.size()-1; i > -1; i--) {
            if (scanned.get(i).isValid() == false) {
                scanned.remove(i);
            }
        }
    }


    void foundDevice(BluetoothDevice device) {
        if (!isScanned(device)) {
            scannDevice(device);
        }
    }


    private void scannDevice(BluetoothDevice device) {
        Device d = new Device(context, device);
        scanned.add(d);
        device.connectGatt(context, true, d);
    }


    private boolean isScanned(BluetoothDevice device) {
        for (Device d : scanned) {
            if (Objects.equals(d.getAddress(), device.getAddress())) return true;
        }
        return false;
    }


    @Override
    public String toString() {


        String s = "";

        if (isEnabled()) {
            String nl = "";

            for (Device d : scanned) {
                if (d.isValid()) {
                    s = s + nl + d.toString();
                    nl = "\n";
                }
            }

            if (s.length() == 0) {
                s = ToDo.translate("No sensors found");
            }
        } else {
            s = ToDo.translate("Bluetooth is disabled");
        }
        return s;
    }
}
