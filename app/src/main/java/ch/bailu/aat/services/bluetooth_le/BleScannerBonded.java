package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

public abstract class BleScannerBonded extends BleScanner {

    private final BluetoothAdapter adapter;


    public BleScannerBonded(BluetoothAdapter a) {
        adapter = a;
    }


    @Override
    public void start() {
        final Set<BluetoothDevice> devices = adapter.getBondedDevices();

        if (devices != null) {
            for (BluetoothDevice d : devices) {
                foundDevice(d);
            }
        }
    }


    @Override
    public void stop() {

    }
}
