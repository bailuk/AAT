package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothGattCharacteristic
import ch.bailu.aat_lib.gpx.information.GpxInformation

interface ServiceInterface {
    fun close()
    fun changed(c: BluetoothGattCharacteristic)
    val isValid: Boolean
    fun discovered(c: BluetoothGattCharacteristic, execute: Executor): Boolean
    fun read(c: BluetoothGattCharacteristic)
    fun getInformation(iid: Int): GpxInformation?
}
