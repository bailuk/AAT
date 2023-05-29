package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothDevice;

public abstract class AbsBleScanner {

    public abstract void start();
    public abstract void stop();

    private final BleSensors sensors;

    public AbsBleScanner(BleSensors s) {
        sensors = s;
    }

    public void foundDevice(BluetoothDevice device) {
        sensors.foundDevice(device);
    }


    public static AbsBleScanner factory(BleSensors sensors) {
        return new BleScanner(sensors);
    }
}
