package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = 18)
public abstract class BleScanner {

    public abstract void start();
    public abstract void stop();

    private final BleSensorsSDK18 sensors;

    public BleScanner(BleSensorsSDK18 s) {
        sensors = s;
    }

    public void foundDevice(BluetoothDevice device) {
        sensors.foundDevice(device);
    }


    public static BleScanner factory(BleSensorsSDK18 sensors) {

        if (Build.VERSION.SDK_INT >= 21) {
            return new BleScannerSDK21(sensors);

        } else {
            return new BleScannerSDK18(sensors);
        }
    }


}
