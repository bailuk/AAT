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
import ch.bailu.aat.util.AppBroadcaster;

@RequiresApi(api = 18)
public class Device extends BluetoothGattCallback implements SensorInterface {

    private final Executer execute = new Executer();

    private final HeartRateService heartRateService;
    private final BatteryService batteryService = new BatteryService();
    private final CscService cscService;

    private final BluetoothDevice device;

    private final Context context;
    private BluetoothGatt gatt = null;

    private int state;

    public Device(ServiceContext c, BluetoothDevice d) {
        device = d;
        context = c.getContext();

        cscService = new CscService(c);
        heartRateService = new HeartRateService(context);
    }


    public synchronized boolean isConnected() {
        return  state == BluetoothProfile.STATE_CONNECTED || 
                state ==BluetoothProfile.STATE_CONNECTING;
    }

    public synchronized boolean isValid() {
        return state == BluetoothProfile.STATE_CONNECTED &&
                (cscService.isValid() || heartRateService.isValid());
    }

    @Override
    public synchronized String toString() {
        String s = device.getName();

        if (cscService.isValid()) {
            s = s + ", " + cscService.toString();
        }

        if (heartRateService.isValid()) {
            s = s+ ", " + heartRateService.toString();
        }

        s = s + ", " + batteryService.getBatteryLevelPercentage() + "%";
        return s;
    }


    @Override
    public synchronized void onConnectionStateChange(BluetoothGatt g, int status, int newState) {
        gatt = g;
        state = newState;
        
        if (status == BluetoothGatt.GATT_SUCCESS && state == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
            
        } else if (state == BluetoothProfile.STATE_DISCONNECTED) {
            broadcast();
        }
    }

    
    private void broadcast() {
        AppBroadcaster.broadcast(context, AppBroadcaster.BLE_DEVICE_SCANNED);
    }

    
    private void executeOrBroadcast(BluetoothGatt gatt) {
        if (!execute.next(gatt)) {
            broadcast();
        }
    }

    
    @Override
    public synchronized void onServicesDiscovered(BluetoothGatt gatt, int status) {
        discover(gatt);

        if ((cscService.isValid() || heartRateService.isValid())) {
            executeOrBroadcast(gatt);
        } else {
            close();
        }
    }




    @Override
    public synchronized void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        executeOrBroadcast(gatt);
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
            batteryService.discovered(c, execute);
            cscService.discovered(c, execute);

        }
    }


    @Override
    public synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic c,
                                     int status) {
        heartRateService.read(c);
        batteryService.read(c);
        cscService.read(c);

        executeOrBroadcast(gatt);
    }


    public synchronized String getAddress() {
        return device.getAddress();
    }


    public synchronized GpxInformation getInformation(int iid) {
        GpxInformation i = heartRateService.getInformation(iid);

        if (i == null) {
            i = cscService.getInformation(iid);
        }

        return i;
    }

    
    @Override
    public synchronized void close() {
        cscService.close();


        if (gatt != null) {
            gatt.close();
            state = BluetoothProfile.STATE_DISCONNECTED;
            gatt = null;
            broadcast();
        }
    }
}
