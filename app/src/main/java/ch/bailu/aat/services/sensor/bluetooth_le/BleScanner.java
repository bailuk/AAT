package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.support.annotation.RequiresApi;

public abstract class BleScanner {

    public abstract void start();
    public abstract void stop();
    public abstract void foundDevice(BluetoothDevice device);


    @RequiresApi(api = 18)
    public static BleScanner factory(BluetoothAdapter adapter, BleSensorsSDK18 devices) {
        /*
        return new BleScannerBonded(adapter) {
            @Override
            public void foundDevice(BluetoothDevice device) {
                devices.foundDevice(device);
            }
        };
        */

        if (Build.VERSION.SDK_INT >= 21) {
            return new BleScannerSDK21(adapter) {
                @Override
                public void foundDevice(BluetoothDevice device) {
                    devices.foundDevice(device);
                }
            };
        } else {
            return new BleScannerSDK18(adapter) {
                @Override
                public void foundDevice(BluetoothDevice device) {
                    devices.foundDevice(device);
                }
            };
        }

    }
}
