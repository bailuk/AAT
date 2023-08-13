package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import java.util.LinkedList
import java.util.Queue
import java.util.UUID

class Executor {
    companion object {
        private val ENABLE_NOTIFICATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }
    private val toReadQ: Queue<BluetoothGattCharacteristic> = LinkedList()
    private val toNotifyQ: Queue<BluetoothGattCharacteristic> = LinkedList()
    private var discovered = false

    @Synchronized
    fun notify(characteristic: BluetoothGattCharacteristic) {
        toNotifyQ.add(characteristic)
    }

    @Synchronized
    fun read(characteristic: BluetoothGattCharacteristic) {
        toReadQ.add(characteristic)
    }

    private fun needToDiscover(): Boolean {
        return !discovered
    }

    @Synchronized
    fun haveToRead(): Boolean {
        return toReadQ.size > 0
    }

    private fun haveToNotify(): Boolean {
        return toNotifyQ.size > 0
    }

    @Synchronized
    fun next(gatt: BluetoothGatt) {
        if (needToDiscover()) {
            discovered = gatt.discoverServices()
        } else if (haveToRead()) {
            gatt.readCharacteristic(toReadQ.poll())
        } else if (haveToNotify()) {
            val head = toNotifyQ.poll()
            if (head != null) enableNotification(gatt, head)
        }
    }

    @Suppress("DEPRECATION")
    private fun enableNotification(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        val descriptor = characteristic.getDescriptor(ENABLE_NOTIFICATION)
        if (descriptor is BluetoothGattDescriptor) {
            gatt.setCharacteristicNotification(characteristic, true)
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }
    }
}
