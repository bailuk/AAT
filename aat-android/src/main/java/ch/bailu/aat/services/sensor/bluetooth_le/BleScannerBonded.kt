package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothAdapter

class BleScannerBonded(sensors: BleSensors) : AbsBleScanner(sensors) {
    private val adapter: BluetoothAdapter?

    init {
        adapter = sensors.adapter
    }

    @Throws(SecurityException::class)
    override fun start() {
        if (adapter == null) return
        val devices = adapter.bondedDevices
        if (devices != null) {
            for (d in devices) {
                foundDevice(d)
            }
        }
    }

    override fun stop() {}
}
