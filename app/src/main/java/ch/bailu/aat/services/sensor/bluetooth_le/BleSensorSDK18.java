package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.services.sensor.list.SensorItemState;
import ch.bailu.aat.util.Timer;

@RequiresApi(api = 18)
public class BleSensorSDK18 extends BluetoothGattCallback implements SensorInterface {

    private final Executer execute = new Executer();

    private final ServiceInterface[] services;
    private final BluetoothDevice device;

    private final SensorList sensorList;
    private final SensorListItem item;

    private final Context context;
    private final BluetoothGatt gatt;

    private boolean closed = false;
    private int closeState;


    private final Timer scanningTimeout = new Timer(new Runnable() {
        @Override
        public void run() {
            if (item.isScanning())  close();
        }
    }, BleSensorsSDK18.SCAN_DURATION);


    private final Timer connectingTimeout = new Timer(new Runnable() {
        @Override
        public void run() {
            if (item.isConnecting())  close();
        }
    }, BleSensorsSDK18.CONNECTING_DURATION);


    public BleSensorSDK18(ServiceContext c, BluetoothDevice d, SensorList l) {
        synchronized (this) {
            sensorList = l;
            device = d;
            context = c.getContext();

            services = new ServiceInterface[]{
                    new CscService(c),
                    new HeartRateService(context)
            };

            item = sensorList.add(this);
            closeState = item.getState();

            gatt = connect();

            if (gatt == null) {
                close();

            } else {
                scanningTimeout.kick();
                connectingTimeout.kick();
                item.setState(SensorItemState.CONNECTING);
                item.setState(SensorItemState.SCANNING);

            }
        }
    }


    private BluetoothGatt connect() {
        if (item.lock(this)) {

            if (Build.VERSION.SDK_INT >= 23) {
                return device.connectGatt(context, true, this,
                        BluetoothDevice.TRANSPORT_LE);
            } else {
                return device.connectGatt(context, true, this);
            }

        }
        return null;
    }


    private void updateListItemName() {
        if (item.isScanning() || item.isConnected())
            item.setName(getName());
    }


    @Override
    public synchronized void onConnectionStateChange(BluetoothGatt g, int status, int state) {
        if (isConnected(status, state)) {
            gatt.discoverServices();

        } else if (!isConnecting(status, state)) {
            close();
        }
    }


    private static boolean isConnected(int status, int state) {
        return (status == BluetoothGatt.GATT_SUCCESS && state == BluetoothProfile.STATE_CONNECTED);
    }

    private static boolean isConnecting(int status, int state) {
        return (status == BluetoothGatt.GATT_SUCCESS && state == BluetoothProfile.STATE_CONNECTING);
    }


    private void executeNextAndSetState(BluetoothGatt gatt) {
        if (execute.haveToRead()) {
            execute.next(gatt);

        } else {
            setNextState();

            if (item.isConnected()) {
                execute.next(gatt);
            }

        }
    }

    private void setNextState() {
        if (item.isScanning()) {
            close(SensorItemState.SUPPORTED);

        } else if (item.isConnecting()) {
            item.setState(SensorItemState.CONNECTED);
            updateListItemName();
            sensorList.broadcast();

        }
    }


    @Override
    public synchronized void onServicesDiscovered(BluetoothGatt gatt, int status) {
        discover(gatt);

        if (hasValidService()) {
            executeNextAndSetState(gatt);

        } else {
            close(SensorItemState.UNSUPPORTED);

        }
    }


    private boolean hasValidService() {
        for (ServiceInterface s : services) {
            if (s.isValid()) return true;
        }
        return false;
    }


    @Override
    public synchronized void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor d, int s) {
        executeNextAndSetState(gatt);
    }

    @Override
    public synchronized void onCharacteristicChanged(BluetoothGatt gatt,
                                                     BluetoothGattCharacteristic c) {

        for (ServiceInterface s : services)
            s.changed(c);

    }



    private void discover(BluetoothGatt gatt) {
        List<BluetoothGattService> list = gatt.getServices();

        for (BluetoothGattService service: list) {
            discover(service);
        }
    }


    private void discover(BluetoothGattService service) {
        List<BluetoothGattCharacteristic> list = service.getCharacteristics();

        for (BluetoothGattCharacteristic c : list) {
            for (ServiceInterface s : services)
                s.discovered(c, execute);
        }
    }


    @Override
    public synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic c,
                                                  int status) {

        for (ServiceInterface s: services)
            s.read(c);

        executeNextAndSetState(gatt);
    }


    public synchronized String getAddress() {
        return device.getAddress();
    }


    @Override
    public String toString() {
        return device.getName()
                + "@"
                + device.getAddress()
                + ":"
                + item.getSensorStateDescription();
    }


    @Override
    public String getName() {
        String name = device.getName();

        if (name == null) name = "";

        for (ServiceInterface s : services) {
            if (s.isValid()) name += " " + s.toString();
        }

        return name + " Sensor";
    }





    public synchronized GpxInformation getInformation(int iid) {
        GpxInformation i = null;

        for (ServiceInterface s : services) {
            i = s.getInformation(iid);
            if (i != null) return i;
        }

        return i;
    }


    private void close(int state) {
        closeState = state;
        close();
    }


    @Override
    public synchronized void close() {
        if (!closed) {
            closed = true;

            scanningTimeout.close();
            connectingTimeout.close();

            updateListItemName();

            for (ServiceInterface s : services) s.close();

            if (gatt != null) {
                gatt.disconnect();
                gatt.close();
            }

            if (item.unlock(this)) {

                item.setState(closeState);
                sensorList.broadcast();
            }
        }
    }
}
