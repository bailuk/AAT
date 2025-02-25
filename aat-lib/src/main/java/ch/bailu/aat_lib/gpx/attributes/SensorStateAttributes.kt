package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.service.sensor.SensorState

class SensorStateAttributes(private val sensors: Int) : GpxAttributes() {
    override fun get(keyIndex: Int): String {
        if (keyIndex == KEY_SENSOR_COUNT) {
            return sensors.toString()
        } else if (keyIndex == KEY_SENSOR_OVERVIEW) {
            return SensorState.overviewString
        }
        return NULL_VALUE
    }

    override fun hasKey(keyIndex: Int): Boolean {
        return KEYS.hasKey(keyIndex)
    }

    override fun size(): Int {
        return KEYS.size()
    }

    override fun getAt(index: Int): String {
        return get(getKeyAt(index))
    }

    override fun getKeyAt(index: Int): Int {
        return KEYS.getKeyIndex(index)
    }

    companion object {
        private val KEYS = Keys()
        val KEY_SENSOR_COUNT = KEYS.add("count")
        val KEY_SENSOR_OVERVIEW = KEYS.add("overview")
    }
}
