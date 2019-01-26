package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.RequiresApi;

import java.util.Set;

@RequiresApi(api = 18)
public class BleScannerBonded extends BleScanner {

    private final BluetoothAdapter adapter;


    public BleScannerBonded(BleSensorsSDK18 sensors) {
        super(sensors);
        adapter = sensors.getAdapter();
    }


    @Override
    public void start() {
        if (adapter == null) return;

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
