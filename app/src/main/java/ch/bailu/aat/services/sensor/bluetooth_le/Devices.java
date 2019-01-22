package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.RequiresApi;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.Sensors;
import ch.bailu.aat.util.ToDo;
import ch.bailu.util_java.util.Objects;

@RequiresApi(api = 18)
public class Devices implements Closeable {

    private final ArrayList<Device> devices = new ArrayList<>(5);




    public synchronized void addAndConnectDevice(ServiceContext scontext, BluetoothDevice device) {
        Device d = new Device(scontext, device);
        devices.add(d);
        device.connectGatt(scontext.getContext(), true, d);
    }


    public synchronized boolean isInList(BluetoothDevice device) {
        for (Device d : devices) {
            if (Objects.equals(d.getAddress(), device.getAddress())) return true;
        }
        return false;
    }



    @Override
    public synchronized String toString() {
        String s = "";
        //String nl = "";

        for (Device d : devices) {
            if (d.isValid()) {
                s = s + d.toString() + "\n";
                //nl = "\n";
            }
        }
        return s;
    }


    public synchronized GpxInformation getInformation(int iid) {
        for (Device device : devices) {
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
        for (Device device : devices) {
            device.close();
        }
        devices.clear();
    }


    public synchronized void closeDisconnectedDevices() {
        for (int i = devices.size()-1; i > -1; i--) {
            final Device device = devices.get(i);

            if (device.isConnected() == false) {
                device.close();
                devices.remove(i);
            }
        }
    }
}
