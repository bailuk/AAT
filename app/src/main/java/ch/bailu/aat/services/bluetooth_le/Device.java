package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.RequiresApi;

import java.util.List;

@RequiresApi(api = 18)
public class Device extends BluetoothGattCallback {

    private final Executer execute = new Executer();

    private final HeartRateService heartRate = new HeartRateService();
    private final BatteryService battery = new BatteryService();
    private final CscService csc = new CscService();

    private final BluetoothDevice device;

    public Device(BluetoothDevice d) {
        device = d;
    }


    public boolean isValid() {
        return csc.isValid() || heartRate.isValid();
    }

    @Override
    public String toString() {
        String s = device.getName();

        if (csc.isValid()) {
            s = s + " " + csc.toString();
        }

        if (heartRate.isValid()) {
            s = s+ " " + heartRate.toString();
        }
        return s;
    }


    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
        }
    }


    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        discover(gatt);

        if (isValid()) {
            execute.next(gatt);
        } else {
            gatt.close();
        }
    }




    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        execute.next(gatt);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic c) {

        heartRate.notify(c);
        csc.notify(c);

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
            heartRate.discovered(c, execute);
            battery.discovered(c, execute);
            csc.discovered(c, execute);

        }
    }


    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic c,
                                     int status) {

        heartRate.read(c);
        battery.read(c);
        csc.read(c);

        execute.next(gatt);
    }

    public String getAddress() {
        return device.getAddress();
    }
}
