package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.RequiresApi;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
/*
@RequiresApi(api = 18)
public class ConnectedSensorList extends ArrayList<SensorSDK18> implements Closeable {


    public ConnectedSensorList() {
        super(5);
    }


    public synchronized void addAndConnectDevice(ServiceContext scontext, BluetoothDevice device) {
        SensorSDK18 d = new SensorSDK18(scontext, device, true);
        add(d);
        device.connectGatt(scontext.getContext(), true, d);
    }


    public synchronized SensorSDK18 find(BluetoothDevice device) {
        return find(device.getAddress());
    }


    public synchronized GpxInformation getInformation(int iid) {
        for (SensorSDK18 device : this) {
            if (device.isValid()) {
                GpxInformation information = device.getInformation(iid);
                if (information != null) return information;
            }
        }
        return null;
    }

    @Override
    public synchronized void close()  {
        closeAllDevices();
    }

    public synchronized void closeAllDevices() {
        for (SensorSDK18 device : this) {
            device.close();
        }
        clear();
    }



    public synchronized void removeDisconnectedDevices() {
        for (int i = size()-1; i > -1; i--) {
            final SensorSDK18 device = get(i);

            if (device.isConnected() == false) {
                device.close();
                remove(i);
            }
        }
    }

    public SensorSDK18 find(String address) {
        for (SensorSDK18 device : this) {
            if (device.getAddress().equalsIgnoreCase(address)) return device;
        }
        return null;
    }
}
*/