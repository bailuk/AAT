package ch.bailu.aat.services.sensor.bluetooth_le

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
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.logger.AppLog

class BleSensor(c: ServiceContext, d: BluetoothDevice, l: SensorList, i: SensorListItem) :
    BluetoothGattCallback(), SensorInterface {

    private val execute = Executor()
    private val services: Array<ServiceInterface>
    private val context: Context = c.getContext()

    private var device: BluetoothDevice
    private var sensorList: SensorList
    private var item: SensorListItem

    private var gatt: BluetoothGatt?
    private var closed = false
    private var closeState = 0
    private val scanningTimeout = AndroidTimer()

    init {
        sensorList = l
        item = i
        device = d
        services = arrayOf(
            CyclingPower(c),
            CscService(c),
            HeartRateService(context),
            BatteryService()
        )
        closeState = item.state
        val gatt = connect()
        if (gatt == null) {
            close()
        } else {
            execute.next(gatt)
            scanningTimeout.kick(BleSensors.SCAN_DURATION) { if (item.isScanning) close() }
            item.state = SensorItemState.CONNECTING
            item.state = SensorItemState.SCANNING
        }
        this.gatt = gatt
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
    override fun onConnectionStateChange(g: BluetoothGatt, status: Int, state: Int) {
        val gatt = gatt

        if (gatt != null && isConnected(status, state)) {
            execute.next(gatt)
        } else if (!isConnecting(status, state)) {
            close()
        }
    }

    private fun executeNextAndSetState(gatt: BluetoothGatt) {
        if (execute.haveToRead()) {
            execute.next(gatt)
        } else {
            setNextState()
            if (item.isConnected) {
                execute.next(gatt)
            }
        }
    }

    private fun setNextState() {
        if (item.isScanning) {
            close(SensorItemState.SUPPORTED)
        } else if (item.isConnecting) {
            item.state = SensorItemState.CONNECTED
            updateListItemName()
            sensorList.broadcast()
        }
    }

    @Synchronized
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        if (discover(gatt)) {
            executeNextAndSetState(gatt)
        } else {
            close(SensorItemState.UNSUPPORTED)
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
        executeNextAndSetState(gatt)
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
        executeNextAndSetState(gatt)
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

    private fun close(state: Int) {
        closeState = state
        close()
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
                item.state = closeState
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
