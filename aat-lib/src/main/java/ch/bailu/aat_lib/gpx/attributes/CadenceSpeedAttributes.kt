package ch.bailu.aat_lib.gpx.attributes

open class CadenceSpeedAttributes(
    val location: String,
    private val isCadenceSensor: Boolean,
    private val isSpeedSensor: Boolean
) : GpxAttributes() {
    var cadenceRpm = 0
    var cadenceRpmAverage = 0
    var circumferenceSI = 0f
    var circumferenceDebugString = "--"

    override fun get(keyIndex: Int): String {
        if (keyIndex == KEY_INDEX_SENSOR_LOCATION) {
            return location
        } else if (keyIndex == KEY_INDEX_CADENCE_SENSOR) {
            return isCadenceSensor.toString()
        } else if (keyIndex == KEY_INDEX_SPEED_SENSOR) {
            return isSpeedSensor.toString()
        } else if (keyIndex == KEY_INDEX_CRANK_RPM) {
            return cadenceRpmAverage.toString()
        } else if (keyIndex == KEY_INDEX_WHEEL_CIRCUMFERENCE) {
            return circumferenceSI.toString()
        } else if (keyIndex == KEY_INDEX_CONTACT) {
            return getAsBoolean(keyIndex).toString()
        } else if (keyIndex == KEY_INDEX_CIRCUMFERENCE_DEBUG) {
            return circumferenceDebugString
        }
        return NULL_VALUE
    }

    override fun getAsBoolean(keyIndex: Int): Boolean {
        return if (keyIndex == KEY_INDEX_CONTACT) {
            val cadenceContact = isCadenceSensor && cadenceRpm > 0
            val speedContact = isSpeedSensor && circumferenceSI > 0f
            cadenceContact || speedContact
        } else {
            super.getAsBoolean(keyIndex)
        }
    }

    override fun hasKey(keyIndex: Int): Boolean {
        return KEYS.hasKey(keyIndex)
    }

    override fun size(): Int {
        return KEYS.size()
    }

    override fun getAt(index: Int): String {
        return get(KEYS.getKeyIndex(index))
    }

    override fun getKeyAt(index: Int): Int {
        return KEYS.getKeyIndex(index)
    }

    companion object {
        @JvmField
        val SENSOR_LOCATION = arrayOf(
            "Other",
            "Top of shoe",
            "In shoe",
            "Hip",
            "Front Wheel",
            "Left Crank",
            "Right Crank",
            "Left Pedal",
            "Right Pedal",
            "Front Hub",
            "Rear Dropout",
            "Chainstay",
            "Rear Wheel",
            "Rear Hub",
            "Chest",
            "Spider",
            "Chain Ring"
        )
        @JvmField
        val KEYS = Keys()
        val KEY_INDEX_SENSOR_LOCATION = KEYS.add("Location")
        val KEY_INDEX_SPEED_SENSOR = KEYS.add("SpeedSensor")
        val KEY_INDEX_CADENCE_SENSOR = KEYS.add("CadenceSensor")
        @JvmField
        val KEY_INDEX_CRANK_RPM = KEYS.add("Cadence")
        val KEY_INDEX_WHEEL_CIRCUMFERENCE = KEYS.add("WheelCircumference")
        @JvmField
        val KEY_INDEX_CONTACT = KEYS.add("Contact")
        val KEY_INDEX_CIRCUMFERENCE_DEBUG = KEYS.add("Debug")
    }
}
