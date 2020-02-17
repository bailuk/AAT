package ch.bailu.aat.services.sensor.bluetooth_le;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

@RequiresApi(api = 18)
public final class Executer {
    private final static UUID ENABLE_NOTIFICATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    private final Queue<BluetoothGattCharacteristic> toReadQ = new LinkedList<BluetoothGattCharacteristic>();
    private final Queue<BluetoothGattCharacteristic> toNotifyQ = new LinkedList<BluetoothGattCharacteristic>();

    private boolean discoverd = false;


    public synchronized void notify(BluetoothGattCharacteristic c) {
        toNotifyQ.add(c);
    }
    public synchronized void read(BluetoothGattCharacteristic c) {
        toReadQ.add(c);
    }

    public boolean needToDiscover() { return !discoverd; }

    public synchronized boolean haveToRead() {
        return toReadQ.size() > 0;
    }

    public boolean haveToNotify() {
        return toNotifyQ.size() > 0;
    }



    public synchronized void next(BluetoothGatt gatt) {
        if (needToDiscover()) {
            discoverd = gatt.discoverServices();

        } else if (haveToRead()) {
            gatt.readCharacteristic(toReadQ.poll());

        } else if (haveToNotify()) {
            enableNotification(gatt, toNotifyQ.poll());

        }
    }


    private void enableNotification(BluetoothGatt gatt, BluetoothGattCharacteristic c) {

        BluetoothGattDescriptor d = c.getDescriptor(ENABLE_NOTIFICATION);

        if (d != null) {
            gatt.setCharacteristicNotification(c, true);
            d.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(d);
        }
    }
}
