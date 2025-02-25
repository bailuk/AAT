package ch.bailu.aat_lib.service.sensor

import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.resources.Res.str


object SensorState {
    private const val SIZE = InfoID.STEP_COUNTER_SENSOR - InfoID.HEART_RATE_SENSOR + 1

    private val connected = IntArray(SIZE)

    private val NAMES = arrayOf(
        str().sensor_heart_rate(),
        str().sensor_power(),
        str().sensor_cadence(),
        str().sensor_speed(),
        str().sensor_barometer(),
        str().sensor_step_counter()
    )

    private val CHARS = charArrayOf(
        'H', 'P', 'C', 'S', 'B', 'T'
    )


    fun toIndex(iid: Int): Int {
        return iid - InfoID.HEART_RATE_SENSOR
    }

    fun isConnected(iid: Int): Boolean {
        return connected[toIndex(iid)] > 0
    }


    fun connect(iid: Int) {
        connected[toIndex(iid)]++
    }

    fun disconnect(iid: Int) {
        connected[toIndex(iid)]--
    }


    fun getName(iid: Int): String {
        return NAMES[toIndex(iid)]
    }

    val overviewString: String
        get() {
            val overview = StringBuilder()
            for (i in 0 until SIZE) {
                if (connected[i] > 0) {
                    overview.append(CHARS[i]).append(connected[i])
                }
            }

            return overview.toString()
        }
}
