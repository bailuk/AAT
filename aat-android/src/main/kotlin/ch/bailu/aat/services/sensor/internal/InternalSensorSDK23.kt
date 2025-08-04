package ch.bailu.aat.services.sensor.internal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.annotation.RequiresApi
import ch.bailu.aat.services.sensor.Connector
import ch.bailu.aat.services.sensor.SensorInterface
import ch.bailu.aat.services.sensor.list.SensorItemState
import ch.bailu.aat.services.sensor.list.SensorListItem

@RequiresApi(api = 23)
abstract class InternalSensorSDK23(
    private val context: Context,
    private val item: SensorListItem,
    sensor: Sensor,
    iid: Int
) : SensorEventListener, SensorInterface {

    override val name: String = InternalSensorsSDK23.toName(sensor)
    private val address: String = InternalSensorsSDK23.toAddress(sensor)
    private var registered = false
    private val connector: Connector = Connector(context, iid)

    init {
        if (item.lock(this)) {
            item.state = SensorItemState.CONNECTING
            item.state = SensorItemState.CONNECTED
            connector.connect()
            requestUpdates(this, sensor)
        }
    }

    /**
     * Was this instance successfully registered with the
     * SensorListItem?
     */
    protected val isLocked: Boolean
        get() = item.isLocked(this)

    override fun toString(): String {
        return name + "@" + address + ":" + item.getSensorStateDescription(context)
    }

    override fun close() {
        if (item.unlock(this)) {
            connector.close()
            cancelUpdates(this)
            item.state = SensorItemState.ENABLED
        }
    }

    private fun requestUpdates(listener: SensorEventListener, sensor: Sensor) {
        val manager = context.getSystemService(SensorManager::class.java)
        if (manager != null) {
            manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            registered = true
        }
    }

    private fun cancelUpdates(listener: SensorEventListener) {
        val manager = context.getSystemService(SensorManager::class.java)
        if (registered) {
            manager.unregisterListener(listener)
            registered = false
        }
    }
}
