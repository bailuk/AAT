package ch.bailu.aat.services.sensor.bluetooth_le

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.os.Build
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.sensor.SensorInterface
import ch.bailu.aat.services.sensor.list.SensorItemState
import ch.bailu.aat.services.sensor.list.SensorList
import ch.bailu.aat.services.sensor.list.SensorListItem
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.logger.AppLog

@SuppressLint("MissingPermission")
class BleSensor(c: ServiceContext, d: BluetoothDevice, l: SensorList, i: SensorListItem) :
    BluetoothGattCallback(), SensorInterface {

    private val execute = Executor()
    private val services: Array<ServiceInterface>
    private val context: Context = c.getContext()

    private var device: BluetoothDevice = d
    private var sensorList: SensorList = l
    private var item: SensorListItem = i

    private var gatt: BluetoothGatt?
    private var closed = false
    private var discovered = false
    private val scanningTimeout = AndroidTimer()

    init {
        services = arrayOf(
            CyclingPower(c),
            CscService(c),
            HeartRateService(context),
            BatteryService()
        )
        item.connectionState = SensorItemState.ConnectionState.IN_PROGRESS
        if (item.shouldScan)
            item.supportedState = SensorItemState.SupportedState.SCANNING
        gatt = connect()
        if (gatt == null) {
            close()
        } else {
            scanningTimeout.kick(BleSensors.SCAN_DURATION) { if (item.isScanning) close() }
        }
    }

    private fun connect(): BluetoothGatt? {
        try {
            if (item.lock(this)) {
                return if (Build.VERSION.SDK_INT >= 23) {
                    device.connectGatt(context, true, this, BluetoothDevice.TRANSPORT_LE)
                } else {
                    device.connectGatt(context, true, this)
                }
            }
        } catch (e: SecurityException) {
            AppLog.e(this, e)
            return null
        }
        return null
    }

    private fun updateListItemName() {
        if (item.isScanning || item.isConnected) item.name = name
    }

    @Synchronized
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, state: Int) {
        if (isConnected(status, state)) {
            item.connectionState = SensorItemState.ConnectionState.YES
            sensorList.broadcast()

            if (!discovered) {
                if (!gatt.discoverServices())
                    close()
                return
            }

            execute.next(gatt)
        } else if (!isConnecting(status, state)) {
            close()
        }
    }

    @Synchronized
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        discovered = true
        if (discover(gatt)) {
            item.supportedState = SensorItemState.SupportedState.YES

            if (item.isEnabled) {
                updateListItemName()
                sensorList.broadcast()

                execute.next(gatt)
            } else
                close()
        } else {
            item.supportedState = SensorItemState.SupportedState.NO
            close()
        }
    }

    private fun discover(gatt: BluetoothGatt): Boolean {
        var discovered = false
        val lists = gatt.services
        for (service in lists) {
            val listc = service.characteristics
            for (s in services) {       // scan each sensor to find valid characteristics
                for (c in listc) {      // characteristics belong to service
                    // found at least one valid new characteristics
                    if (s.discovered(c, execute)) discovered = true
                }
            }
        }
        return discovered
    }

    @Synchronized
    override fun onDescriptorWrite(gatt: BluetoothGatt, d: BluetoothGattDescriptor, s: Int) {
        execute.next(gatt)
    }

    @Deprecated("Deprecated in Java")
    @Synchronized
    override fun onCharacteristicChanged(
        gatt: BluetoothGatt,
        c: BluetoothGattCharacteristic
    ) {
        for (s in services) s.changed(c)
    }

    @Deprecated("Deprecated in Java")
    @Synchronized
    override fun onCharacteristicRead(
        gatt: BluetoothGatt, c: BluetoothGattCharacteristic,
        status: Int
    ) {
        for (s in services) s.read(c)
        execute.next(gatt)
    }

    override fun toString(): String {
        return (device.name
                + "@"
                + device.address
                + ":"
                + item.getSensorStateDescription(context))
    }

    override val name: String
        get() {
            val builder = StringBuilder(20)
            if (device.name != null) builder.append(device.name)
            for (s in services) {
                if (s.isValid) builder.append(" ").append(s)
            }
            return builder.toString()
        }

    @Synchronized
    override fun getInformation(iid: Int): GpxInformation? {
        for (s in services) {
            if (s.isValid) {
                val i = s.getInformation(iid)
                if (i != null) return i
            }
        }
        return null
    }

    @Synchronized
    override fun close() {
        if (!closed) {
            closed = true
            scanningTimeout.cancel()
            updateListItemName()
            for (s in services) s.close()

            val gatt = gatt
            if (gatt != null) {
                gatt.disconnect()
                gatt.close()
            }

            if (item.unlock(this)) {
                item.connectionState = SensorItemState.ConnectionState.NO
                if (item.isScanning)
                    item.supportedState = SensorItemState.SupportedState.UNKNOWN
                sensorList.broadcast()
            }
        }
    }

    companion object {
        private fun isConnected(status: Int, state: Int): Boolean {
            return status == BluetoothGatt.GATT_SUCCESS && state == BluetoothProfile.STATE_CONNECTED
        }

        private fun isConnecting(status: Int, state: Int): Boolean {
            return status == BluetoothGatt.GATT_SUCCESS && state == BluetoothProfile.STATE_CONNECTING
        }
    }
}
