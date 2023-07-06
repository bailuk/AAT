package ch.bailu.aat.services.sensor.internal

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.annotation.RequiresApi
import ch.bailu.aat.services.sensor.bluetooth_le.Broadcaster
import ch.bailu.aat.services.sensor.list.SensorListItem
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.attributes.SensorInformation
import ch.bailu.aat_lib.gpx.attributes.StepCounterAttributes
import kotlin.math.roundToInt

@RequiresApi(api = 23)
class StepCounterSensor(c: Context, item: SensorListItem, sensor: Sensor) : InternalSensorSDK23(
    c, item, sensor, InfoID.STEP_COUNTER_SENSOR
) {
    private var first: Sample = Sample.NULL
    private val samples = Array(SAMPLES) { Sample.NULL }
    private var index = 0
    private val broadcaster: Broadcaster
    private var information: GpxInformation? = null

    init {
        broadcaster = Broadcaster(c, InfoID.STEP_COUNTER_SENSOR)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val attr = StepCounterAttributes()
        addSample(event)
        setAttributes(attr)
        information = SensorInformation(attr)
        broadcaster.broadcast()
    }

    private fun setAttributes(attr: StepCounterAttributes) {
        attr.stepsTotal = totalSteps
        attr.stepsRate = stepsRate
    }

    private val totalSteps: Int
        get() = samples[index].steps - first.steps

    private fun addSample(event: SensorEvent) {
        index++
        index %= samples.size
        samples[index] = Sample(event)
        if (first === Sample.NULL) {
            first = samples[index]
        }
    }

    private val firstSample: Sample
        get() {
            var index = index + 1
            for (i in samples.indices) {
                index %= samples.size
                if (samples[index] !== Sample.NULL) {
                    return samples[index]
                }
                index++
            }
            return Sample.NULL
        }

    private val stepsRate: Int
        get() {
            val a = firstSample
            val b = samples[index]
            val timeDelta = b.timeMillis - a.timeMillis
            val steps = b.steps - a.steps

            return if (timeDelta > 0 && steps > 0) {
                (steps * 1000 * 60 / timeDelta.toFloat()).roundToInt()
            } else 0
        }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    override fun getInformation(iid: Int): GpxInformation? {
        return if (iid == InfoID.STEP_COUNTER_SENSOR) information else null
    }

    private class Sample {
        val timeMillis: Long
        val steps: Int

        constructor(event: SensorEvent) {
            timeMillis = event.timestamp / 1000000
            steps = event.values[0].toInt()
        }

        private constructor() {
            timeMillis = 0
            steps = 0
        }

        companion object {
            val NULL = Sample()
        }
    }

    companion object {
        private const val SAMPLES = 25
    }
}
