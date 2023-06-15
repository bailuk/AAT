package ch.bailu.aat.services.sensor

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.sensor.list.SensorList
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.util.WithStatusText

class SensorService(sc: ServiceContext) : VirtualService(), WithStatusText, SensorServiceInterface {
    val sensorList: SensorList
    private val bluetoothLE: Sensors
    private val internal: Sensors
    private val broadcaster: Broadcaster
    private val context: Context
    private val onBluetoothStateChanged: android.content.BroadcastReceiver =
        object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val state = intent.getIntExtra(BluetoothAdapter.ACTION_STATE_CHANGED, 0)
                if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_OFF) {
                    updateConnections()
                }
            }
        }
    private val onSensorDisconnected =
        ch.bailu.aat_lib.dispatcher.BroadcastReceiver { _: Array<String?>? -> updateConnections() }
    private val onSensorReconnect = ch.bailu.aat_lib.dispatcher.BroadcastReceiver { _: Array<String?>? ->
        updateConnections()
        scan() // rescan to get them in cache if they were not
    }

    init {
        context = sc.context
        sensorList = SensorList(sc.context)
        bluetoothLE = Sensors.factoryBle(sc, sensorList)
        internal = Sensors.factoryInternal(sc.context, sensorList)
        broadcaster = AndroidBroadcaster(sc.context)
        AndroidBroadcaster.register(
            context,
            onBluetoothStateChanged,
            BluetoothAdapter.ACTION_STATE_CHANGED
        )
        broadcaster.register(
            onSensorDisconnected,
            AppBroadcaster.SENSOR_DISCONNECTED + InfoID.SENSORS
        )
        broadcaster.register(onSensorReconnect, AppBroadcaster.SENSOR_RECONNECT + InfoID.SENSORS)
        updateConnections()
    }

    override fun appendStatusText(builder: StringBuilder) {
        builder.append(this)
    }

    @Synchronized
    fun close() {
        bluetoothLE.close()
        internal.close()
        sensorList.close()
        context.unregisterReceiver(onBluetoothStateChanged)
        broadcaster.unregister(onSensorDisconnected)
        broadcaster.unregister(onSensorReconnect)
    }

    @Synchronized
    override fun updateConnections() {
        bluetoothLE.updateConnections()
        internal.updateConnections()
        sensorList.broadcast()
    }

    @Synchronized
    override fun scan() {
        try {
            bluetoothLE.scan()
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
    }

    @Synchronized
    override fun toString(): String {
        return bluetoothLE.toString()
    }

    @Synchronized
    override fun getInfo(iid: Int): GpxInformation {
        val info = getInformationOrNull(iid)
        return if (info is GpxInformation) {
            info
        } else {
            GpxInformation.NULL
        }
    }

    @Synchronized
    override fun getInformationOrNull(iid: Int): GpxInformation? {
        return sensorList.getInformation(iid)
    }
}
