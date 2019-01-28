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
import ch.bailu.aat.services.sensor.list.SensorStateID;

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


    private boolean closed = false;

    private int closeState;


    public BleSensorSDK18(ServiceContext c, BluetoothDevice d, SensorList l) {
        sensorList = l;
        device = d;
        context = c.getContext();
        cscService = new CscService(c);
        heartRateService = new HeartRateService(context);


        item = getItem();
        closeState = item.getState();

        if (item.lock(this)) {
            gatt = device.connectGatt(context, false, this);

        } else {
            gatt = null;

        }


        if (gatt == null) {
            close();

        } else {
            item.setState(SensorStateID.CONNECTING);
            item.setState(SensorStateID.SCANNING);

        }
    }


    private SensorListItem getItem() {
        if (item == null) item = sensorList.add(this);
        else item.setName(getName());
        return item;
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


    private void executeNextOrClose(BluetoothGatt gatt) {
        if (execute.haveToRead()) {
            execute.next(gatt);

        } else {
            item = getItem();
            if (item.isEnabled()) {
                execute.next(gatt);

            } else {

                closeState = SensorStateID.VALID;
                close();
            }
        }
    }



    @Override
    public synchronized void onServicesDiscovered(BluetoothGatt gatt, int status) {
        discover(gatt);

        if (cscService.isValid() || heartRateService.isValid()) {
            executeNextOrClose(gatt);

        } else {
            closeState = SensorStateID.INVALID;
            close();
        }
    }


    @Override
    public synchronized void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor d, int s) {
        executeNextOrClose(gatt);
    }

    @Override
    public synchronized void onCharacteristicChanged(BluetoothGatt gatt,
                                                     BluetoothGattCharacteristic c) {


        heartRateService.notify(c);
        cscService.notify(c);

        getItem().setState(SensorStateID.CONNECTED);
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

        executeNextOrClose(gatt);
    }


    public synchronized String getAddress() {
        return device.getAddress();
    }


    @Override
    public String toString() {
        String s = getName();
        if (s == null)
            s = super.toString();

        return s;
    }


    @Override
    public String getName() {
        String name = device.getName();

        if (name == null) name = "";

        if (heartRateService.isValid()) name += " " + heartRateService.toString();
        if (cscService.isValid()) name += " " + cscService.toString();

        return name + " Sensor";

    }


    public synchronized GpxInformation getInformation(int iid) {
        GpxInformation i = heartRateService.getInformation(iid);

        if (i == null) {
            i = cscService.getInformation(iid);
        }

        return i;
    }


    @Override
    public boolean isConnected() {
        return heartRateService.isConnectionEstablished() || cscService.isConnectionEstablished();
    }


    @Override
    public synchronized void close() {
        if (!closed) {
            closed = true;

            item = getItem();

            cscService.close();
            heartRateService.close();

            if (gatt != null) {
                gatt.close();
            }

            if (item.unlock(this)) {

                item.setState(closeState);
                sensorList.broadcast();
            }
        }
    }
}
