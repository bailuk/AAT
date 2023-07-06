package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid

class BleScanner internal constructor(sensors: BleSensors) : AbsBleScanner(sensors) {
    private val adapter: BluetoothAdapter?

    private val callback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            foundDevice(result.device)
        }
    }
    private val settings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // also possible SCAN_MODE_BALANCED, SCAN_MODE_LOW_LATENCY
        .build()
    private val hrFilter = ScanFilter.Builder()
        .setServiceUuid(ParcelUuid.fromString("0000180d-0000-1000-8000-00805f9b34fb"))
        .build()
    private val cscFilter = ScanFilter.Builder()
        .setServiceUuid(ParcelUuid.fromString("00001816-0000-1000-8000-00805f9b34fb"))
        .build()
    private val filters = ArrayList(listOf(hrFilter, cscFilter))

    init {
        adapter = sensors.adapter
    }

    override fun start() {
        if (adapter != null) {
            val scanner = adapter.bluetoothLeScanner
            scanner?.startScan(filters, settings, callback)
        }
    }

    override fun stop() {
        if (adapter != null) {
            val scanner = adapter.bluetoothLeScanner
            scanner?.stopScan(callback)
        }
    }
}
