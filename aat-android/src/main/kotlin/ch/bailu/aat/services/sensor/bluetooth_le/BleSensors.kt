package ch.bailu.aat.services.sensor.bluetooth_le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.sensor.Sensors
import ch.bailu.aat.services.sensor.bluetooth_le.AbsBleScanner.Companion.factory
import ch.bailu.aat.services.sensor.list.SensorList
import ch.bailu.aat.services.sensor.list.SensorListItem
import ch.bailu.aat.util.AndroidTimer

@SuppressLint("MissingPermission")
class BleSensors(private val scontext: ServiceContext, private val sensorList: SensorList) : Sensors() {

    val adapter: BluetoothAdapter = getAdapter(scontext.getContext())

    private val scannerBonded: AbsBleScanner = BleScannerBonded(this)
    private val scannerBle: AbsBleScanner = factory(this)
    private var scanning = false
    private val timer = AndroidTimer()

    @Synchronized
    @Throws(SecurityException::class)
    override fun scan() {
        stopScanner()
        if (isEnabled) {
            startScanner()
        }
    }

    @Synchronized
    override fun updateConnections() {
        sensorList.forEach { item ->
            if (item.isBluetoothDevice) {
                if (isEnabled) {
                    if (item.isEnabled && !item.isOpen) {
                        connect(item)
                    }
                } else {
                    item.close()
                }
            }
        }
    }

    private fun connect(item: SensorListItem) {
        val device = adapter.getRemoteDevice(item.address)
        device?.let { BleSensor(scontext, it, sensorList, item) }
    }

    private fun getAdapter(context: Context): BluetoothAdapter {
        val bm = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bm.adapter
    }

    private val isEnabled: Boolean
        get() = adapter.isEnabled

    @Synchronized
    fun foundDevice(device: BluetoothDevice) {
        val displayName = device.name ?: device.address
        val item = sensorList.add(device.address, displayName)
        if (item.isUnscannedOrScanning) {
            BleSensor(scontext, device, sensorList, item)
        }
    }

    @Synchronized
    override fun toString(): String {
        return if (isEnabled) {
            if (scanning) {
                getString(R.string.sensor_bl_scanning)
            } else {
                getString(R.string.sensor_bl_enabled)
            }
        } else {
            getString(R.string.sensor_bl_disabled)
        }
    }

    private fun getString(r: Int): String {
        return scontext.getContext().getString(r)
    }

    @Synchronized
    override fun close() {
        stopScanner()
    }

    @Throws(SecurityException::class)
    private fun startScanner() {
        scannerBonded.start()
        timer.kick(SCAN_DURATION) { stopScanner() }
        scannerBle.start()
        scanning = isEnabled
        sensorList.broadcast()
    }

    private fun stopScanner() {
        scanning = false
        timer.cancel()
        scannerBle.stop()
        sensorList.broadcast()
    }

    companion object {
        const val SCAN_DURATION = (10 * 1000).toLong()
    }
}
