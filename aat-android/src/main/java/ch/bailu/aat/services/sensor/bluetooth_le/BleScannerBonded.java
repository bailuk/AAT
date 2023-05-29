package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

public final class BleScannerBonded extends AbsBleScanner {

    private final BluetoothAdapter adapter;

    public BleScannerBonded(BleSensors sensors) {
        super(sensors);
        adapter = sensors.getAdapter();
    }


    @Override
    public void start() throws SecurityException {
        if (adapter == null) return;

        final Set<BluetoothDevice> devices = adapter.getBondedDevices();

        if (devices != null) {
            for (BluetoothDevice d : devices) {
                foundDevice(d);
            }
        }
    }

    @Override
    public void stop() {}
}
