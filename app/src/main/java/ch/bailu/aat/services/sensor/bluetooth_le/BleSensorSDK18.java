package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.annotation.RequiresApi;

import java.util.List;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.SensorInterface;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.services.sensor.list.SensorListItem;

@RequiresApi(api = 18)
public class BleSensorSDK18 extends BluetoothGattCallback implements SensorInterface {

    private final Executer execute = new Executer();

    private final HeartRateService heartRateService;
    private final CscService cscService;

    private final BluetoothDevice device;

    private final SensorList sensorList;
    private SensorListItem item;

    private final Context context;
    private final BluetoothGatt gatt;

    private boolean connectionEstablished =false;


    private boolean closed = false;


    public BleSensorSDK18(ServiceContext c, BluetoothDevice d, SensorList l) {
        sensorList = l;
        device = d;
        context = c.getContext();
        cscService = new CscService(c);
        heartRateService = new HeartRateService(context);

        gatt = connect();

        if (gatt == null) {
            close();
        }
    }


    private BluetoothGatt connect() {
        item = sensorList.find(getAddress());

        if (item == null) {
            return device.connectGatt(context, false, this);

        } else if(item.isEnabled() && item.isConnected() == false) {
            item.setSensor(this);
            return device.connectGatt(context, false, this);
        }
        return null;
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


    private void executeOrBroadcast(BluetoothGatt gatt) {
        execute.next(gatt);
        if (!execute.next(gatt) && sensorList.find(getAddress()) != null) {
            sensorList.broadcast();
        }
    }

    
    @Override
    public synchronized void onServicesDiscovered(BluetoothGatt gatt, int status) {
        discover(gatt);

        if (cscService.isValid() || heartRateService.isValid()) {

            item = sensorList.add(this);
            item.setSensor(this);

            if (item.isEnabled()) {
                connectionEstablished = true;
                execute.next(gatt);
                //executeOrBroadcast(gatt);

            } else {
                close();

            }

            sensorList.broadcast();
        } else {
            close();
        }
    }


    @Override
    public synchronized void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        execute.next(gatt);
        //executeOrBroadcast(gatt);
    }

    @Override
    public synchronized void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic c) {


        heartRateService.notify(c);
        cscService.notify(c);
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
            heartRateService.discovered(c, execute);
            cscService.discovered(c, execute);

        }
    }


    @Override
    public synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic c,
                                     int status) {
        heartRateService.read(c);
        cscService.read(c);

        execute.next(gatt);
        //executeOrBroadcast(gatt);
    }


    public synchronized String getAddress() {
        return device.getAddress();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        String name = device.getName();
        if (heartRateService.isValid()) name += " " + heartRateService.toString();
        if (cscService.isValid()) name += " " + cscService.toString();

        return  name;
    }


    public synchronized GpxInformation getInformation(int iid) {
        GpxInformation i = heartRateService.getInformation(iid);

        if (i == null) {
            i = cscService.getInformation(iid);
        }

        return i;
    }

    @Override
    public boolean isConnectionEstablished() {
        return connectionEstablished;
    }
    
    @Override
    public synchronized void close() {
        if (!closed) {
            closed = true;
            connectionEstablished = false;

            cscService.close();

            if (gatt != null) {
                gatt.close();
            }

            if (item != null) {
                item.close();
                sensorList.broadcast();
            }
        }
    }
}
