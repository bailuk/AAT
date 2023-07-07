package ch.bailu.aat_lib.gpx.attributes

class StepCounterAttributes : GpxAttributes() {
    var stepsRate = 0
    var stepsTotal = 0
    override fun get(keyIndex: Int): String {
        return getAsInteger(keyIndex).toString()
    }

    override fun getAsInteger(keyIndex: Int): Int {
        if (keyIndex == KEY_INDEX_STEPS_RATE) {
            return stepsRate
        } else if (keyIndex == KEY_INDEX_STEPS_TOTAL) {
            return stepsTotal
        }
        return 0
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
        val KEY_INDEX_STEPS_RATE = KEYS.add("StepRate")
        @JvmField
        val KEY_INDEX_STEPS_TOTAL = KEYS.add("TotalSteps")
    }
}
