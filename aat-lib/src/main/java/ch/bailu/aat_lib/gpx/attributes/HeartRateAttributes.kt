package ch.bailu.aat_lib.gpx.attributes

open class HeartRateAttributes(val location: String = BODY_SENSOR_LOCATIONS[0]) :
    GpxAttributes() {
    private var bpm = 0
    var haveSensorContact = false
    var rrInterval = 0
    fun setBpm(bpm: Int) {
        if (bpm > BPM_MIN && bpm < BPM_MAX) {
            this.bpm = bpm
        } else {
            this.bpm = 0
        }
    }

    fun haveBpm(): Boolean {
        return bpm > 0
    }

    override fun get(keyIndex: Int): String {
        if (keyIndex == KEY_INDEX_BPM) {
            return bpm.toString()
        } else if (keyIndex == KEY_INDEX_RR) {
            return (rrInterval / 1024f).toString()
        } else if (keyIndex == KEY_INDEX_CONTACT) {
            return if (haveSensorContact) "" else "…"
        } else if (keyIndex == KEY_INDEX_LOCATION) {
            return location
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
        return get(KEYS.getKeyIndex(index))
    }

    override fun getKeyAt(index: Int): Int {
        return KEYS.getKeyIndex(index)
    }

    companion object {
        private val KEYS = Keys()
        @JvmField
        val KEY_INDEX_BPM = KEYS.add("HeartRate")
        val KEY_INDEX_RR = KEYS.add("RR")
        val KEY_INDEX_CONTACT = KEYS.add("Contact")
        val KEY_INDEX_LOCATION = KEYS.add("Location")
        val BODY_SENSOR_LOCATIONS = arrayOf(
            "Other",
            "Chest",
            "Wrist",
            "Finger",
            "Hand",
            "Ear Lobe",
            "Foot"
        )
        private const val BPM_MIN = 30
        private const val BPM_MAX = 300
    }
}
