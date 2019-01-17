package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.RequiresApi;

import java.util.Stack;
import java.util.UUID;

@RequiresApi(api = 18)
public class Executer {
    private final UUID ENABLE_NOTIFICATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private final Stack<BluetoothGattCharacteristic> toRead = new Stack<>();
    private final Stack<BluetoothGattCharacteristic> toNotify = new Stack<>();


    public void notify(BluetoothGattCharacteristic c) {
        toNotify.push(c);
    }


    public void read(BluetoothGattCharacteristic c) {
        toRead.push(c);
    }


    public void next(BluetoothGatt gatt) {
        if (toRead.size() > 0) {
            gatt.readCharacteristic(toRead.pop());

        } else if (toNotify.size() > 0) {
            enableNotification(gatt, toNotify.pop());
        }
    }


    private void enableNotification(BluetoothGatt gatt, BluetoothGattCharacteristic c) {
        gatt.setCharacteristicNotification(c, true);

        for (BluetoothGattDescriptor d : c.getDescriptors()) {
            if (ENABLE_NOTIFICATION.equals(d.getUuid())) {
                d.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(d);
                break;
            }
        }
    }
}
