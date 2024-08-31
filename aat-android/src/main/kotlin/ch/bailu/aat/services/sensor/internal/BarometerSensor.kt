package ch.bailu.aat.services.sensor.internal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.annotation.RequiresApi
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.services.sensor.list.SensorListItem
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import ch.bailu.aat_lib.preferences.location.SolidPressureAtSeaLevel
import ch.bailu.aat_lib.preferences.location.SolidProvideAltitude

@RequiresApi(api = 23)
class BarometerSensor(private val context: Context, item: SensorListItem, sensor: Sensor) :
    InternalSensorSDK23(
        context, item, sensor, InfoID.BAROMETER_SENSOR
    ), OnPreferencesChanged {
    private val hypsometric = Hypsometric()
    private val spressure: SolidPressureAtSeaLevel = SolidPressureAtSeaLevel(Storage(context))
    private val saltitude: SolidProvideAltitude = SolidProvideAltitude(Storage(context), SolidUnit.SI)
    private var information: GpxInformation? = null

    init {
        if (isLocked) {
            hypsometric.pressureAtSeaLevel = spressure.pressure.toDouble()
            spressure.register(this)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val pressure = getPressure(event)
        hypsometric.setPressure(pressure.toDouble())
        information = Information(hypsometric.altitude, pressure)
        AndroidBroadcaster.broadcast(context, changedAction)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (saltitude.hasKey(key)) {
            hypsometric.altitude = saltitude.getValue().toDouble()
            if (hypsometric.isPressureAtSeaLevelValid) {
                spressure.pressure = hypsometric.pressureAtSeaLevel.toFloat()
            }
        } else if (spressure.hasKey(key)) {
            hypsometric.pressureAtSeaLevel = spressure.pressure.toDouble()
        }
    }

    override fun getInformation(iid: Int): GpxInformation? {
        return if (iid == InfoID.BAROMETER_SENSOR) information else null
    }

    override fun close() {
        if (isLocked) {
            spressure.unregister(this)
        }
        super.close()
    }

    class Information(a: Double, p: Float) : GpxInformation() {
        private val attributes: GpxAttributes = Attributes(p)
        private val altitude: Double = a
        private val time = System.currentTimeMillis()

        override fun getAttributes(): GpxAttributes {
            return attributes
        }

        override fun getAltitude(): Double {
            return altitude
        }

        override fun getTimeStamp(): Long {
            return time
        }
    }

    class Attributes(private val pressure: Float) : GpxAttributes() {
        override fun get(keyIndex: Int): String {
            return if (keyIndex == KEY_INDEX_PRESSURE) pressure.toString() else NULL_VALUE
        }

        override fun hasKey(keyIndex: Int): Boolean {
            return keyIndex == KEY_INDEX_PRESSURE
        }

        override fun size(): Int {
            return 1
        }

        override fun getAt(index: Int): String {
            return get(KEY_INDEX_PRESSURE)
        }

        override fun getKeyAt(index: Int): Int {
            return KEY_INDEX_PRESSURE
        }

        companion object {
            val KEY_INDEX_PRESSURE = Keys.toIndex("Pressure")
        }
    }

    companion object {
        private val changedAction = AppBroadcaster.SENSOR_CHANGED + InfoID.BAROMETER_SENSOR
        fun getPressure(event: SensorEvent): Float {
            return if (event.values.isNotEmpty()) {
                event.values[0]
            } else 0f
        }
    }
}
