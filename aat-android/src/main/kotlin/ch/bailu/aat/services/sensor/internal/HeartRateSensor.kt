package ch.bailu.aat.services.sensor.internal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.annotation.RequiresApi
import ch.bailu.aat.services.sensor.bluetooth_le.Broadcaster
import ch.bailu.aat.services.sensor.list.SensorListItem
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.attributes.HeartRateAttributes
import ch.bailu.aat_lib.gpx.attributes.SensorInformation

@RequiresApi(api = 23)
class HeartRateSensor(c: Context, item: SensorListItem, sensor: Sensor) :
    InternalSensorSDK23(c, item, sensor, InfoID.HEART_RATE_SENSOR) {
    private val attributes: HeartRateAttributes
    private var information: GpxInformation? = null
    private val broadcaster: Broadcaster

    init {
        broadcaster = Broadcaster(c, InfoID.HEART_RATE_SENSOR)
        attributes = HeartRateAttributes()
    }

    override fun onSensorChanged(event: SensorEvent) {
        attributes.setBpm(toBpm(event))
        attributes.haveSensorContact = toContact(event)
        information = SensorInformation(attributes)
        if (attributes.haveBpm()) {
            broadcaster.broadcast()
        } else if (!attributes.haveSensorContact) {
            broadcaster.broadcast()
        } else if (broadcaster.timeout()) {
            broadcaster.broadcast()
        }
    }

    private fun toBpm(event: SensorEvent?): Int {
        return if (event != null && event.values.isNotEmpty()) {
            event.values[0].toInt()
        } else 0
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        attributes.haveSensorContact = toContact(accuracy)
        broadcaster.broadcast()
    }

    override fun getInformation(iid: Int): GpxInformation? {
        return if (iid == InfoID.HEART_RATE_SENSOR) information else null
    }

    companion object {
        private fun toContact(event: SensorEvent?): Boolean {
            return event != null && toContact(event.accuracy)
        }

        private fun toContact(accuracy: Int): Boolean {
            return accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE &&
                    accuracy != SensorManager.SENSOR_STATUS_NO_CONTACT
        }
    }
}
