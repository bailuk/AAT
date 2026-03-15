package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothGattCharacteristic
import ch.bailu.aat_lib.gpx.information.GpxInformation

/**
 * Contract for a BLE GATT sensor service (e.g. Cycling Power, Heart Rate).
 *
 * Lifecycle: [discovered] registers characteristics -> [read] captures static
 * features -> [changed] delivers live measurements -> [getInformation] lets
 * callers query the latest [GpxInformation] by [InfoID].
 */
interface ServiceInterface {
    fun close()
    fun changed(c: BluetoothGattCharacteristic)
    val isValid: Boolean
    fun discovered(c: BluetoothGattCharacteristic, execute: Executor): Boolean
    fun read(c: BluetoothGattCharacteristic)
    fun getInformation(iid: Int): GpxInformation?
}
