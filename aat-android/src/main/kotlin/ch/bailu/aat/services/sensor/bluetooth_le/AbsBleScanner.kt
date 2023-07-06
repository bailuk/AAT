package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothDevice

abstract class AbsBleScanner(private val sensors: BleSensors) {
    abstract fun start()
    abstract fun stop()

    fun foundDevice(device: BluetoothDevice) {
        sensors.foundDevice(device)
    }

    companion object {
        @JvmStatic
        fun factory(sensors: BleSensors): AbsBleScanner {
            return BleScanner(sensors)
        }
    }
}
