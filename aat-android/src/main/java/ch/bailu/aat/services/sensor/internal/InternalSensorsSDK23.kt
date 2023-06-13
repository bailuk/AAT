package ch.bailu.aat.services.sensor.internal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.annotation.RequiresApi
import ch.bailu.aat.services.sensor.Sensors
import ch.bailu.aat.services.sensor.list.SensorItemState
import ch.bailu.aat.services.sensor.list.SensorList
import ch.bailu.aat.services.sensor.list.SensorListItem

@RequiresApi(api = 23)
class InternalSensorsSDK23(private val context: Context, private val sensorList: SensorList) :
    Sensors() {
    private val manager: SensorManager = context.getSystemService(SensorManager::class.java)

    init {
        scan()
    }

    override fun scan() {
        scan(Sensor.TYPE_STEP_COUNTER)
        scan(Sensor.TYPE_PRESSURE)
        scan(Sensor.TYPE_HEART_RATE)
    }

    private fun scan(type: Int) {
        val sensors = manager.getSensorList(type)
        if (sensors != null) {
            for (sensor in sensors) {
                val item = sensorList.add(toAddress(sensor), toName(sensor))
                if (item.state == SensorItemState.UNSCANNED) {
                    item.state = SensorItemState.SCANNING
                    if (isSupported(sensor)) {
                        item.state = SensorItemState.SUPPORTED
                    } else {
                        item.state = SensorItemState.UNSUPPORTED
                    }
                }
            }
        }
    }

    private fun isSupported(sensor: Sensor): Boolean {
        return sensor.type == Sensor.TYPE_PRESSURE || sensor.type == Sensor.TYPE_HEART_RATE || sensor.type == Sensor.TYPE_STEP_COUNTER
    }

    override fun updateConnections() {
        sensorList.forEach {
            if (it.isEnabled && !it.isConnected) {
                factory(it.address, it)
            }
        }
    }

    private fun factory(address: String, item: SensorListItem): InternalSensorSDK23? {
        val sensors = manager.getSensorList(Sensor.TYPE_ALL)
        if (sensors != null) {
            for (sensor in sensors) {
                if (address == toAddress(sensor)) {
                    return factory(sensor, item)
                }
            }
        }
        return null
    }

    private fun factory(sensor: Sensor, item: SensorListItem): InternalSensorSDK23? {
        if (sensor.type == Sensor.TYPE_HEART_RATE) return HeartRateSensor(
            context,
            item,
            sensor
        ) else if (sensor.type == Sensor.TYPE_PRESSURE) return BarometerSensor(
            context,
            item,
            sensor
        ) else if (sensor.type == Sensor.TYPE_STEP_COUNTER) return StepCounterSensor(
            context,
            item,
            sensor
        )
        return null
    }

    companion object {
        fun toName(sensor: Sensor): String {
            return sensor.vendor + " " + sensor.name
        }

        fun toAddress(sensor: Sensor): String {
            return sensor.vendor + sensor.name
        }
    }
}
