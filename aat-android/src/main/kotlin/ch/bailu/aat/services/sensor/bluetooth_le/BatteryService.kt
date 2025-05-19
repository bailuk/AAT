package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothGattCharacteristic
import ch.bailu.aat_lib.gpx.information.GpxInformation
import java.util.UUID

class BatteryService : ServiceInterface {
    override var isValid = false
        private set

    private var batteryLevelPercentage = 0

    override fun close() {}

    override fun changed(c: BluetoothGattCharacteristic) {}


    override fun discovered(c: BluetoothGattCharacteristic, execute: Executor): Boolean {
        var disc = false
        if (BATTERY_SERVICE == c.service.uuid && BATTERY_LEVEL == c.uuid) {
            isValid = true
            disc = true
            execute.read(c)
        }
        return disc
    }

    @Suppress("DEPRECATION")
    override fun read(c: BluetoothGattCharacteristic) {
        if (BATTERY_SERVICE == c.service.uuid && BATTERY_LEVEL == c.uuid) {
            logBatteryLevel(c.value)
        }
    }

    private fun logBatteryLevel(value: ByteArray) {
        batteryLevelPercentage = value[0].toInt()
    }

    override fun getInformation(iid: Int): GpxInformation? {
        return null
    }

    override fun toString(): String {
        return "Battery=$batteryLevelPercentage%"
    }

    companion object {
        val BATTERY_SERVICE: UUID = ID.toUUID(0x180f)
        val BATTERY_LEVEL: UUID = ID.toUUID(0x2A19)
    }
}
