package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.GpxPointNode

/**
 * Rolling 10-minute averages for speed and power.
 *
 * Maintains a sliding window over the [GpxPointNode]s,
 * accumulating distance, time, and power×time as each point is added via
 * [update]. When the window exceeds 10 minutes, [trim] pops entries
 * from the front.
 */
class TimeWindowAttributes : GpxSubAttributes(KEYS) {

    private class Sample(val timeMillis: Long, val distance: Float, val energy: Double)

    private val samples = ArrayDeque<Sample>()

    /** Accumulated time within the window, in milliseconds. */
    private var windowTimeMillis = 0L

    /** Accumulated distance within the window, in metres. */
    private var distance = 0f

    /** Accumulated energy (sum of watts × milliseconds) within the window. */
    private var energy = 0.0

    override fun get(keyIndex: Int): String {
        return when (keyIndex) {
            INDEX_WINDOW_SPEED -> getAsFloat(keyIndex).toString()
            INDEX_WINDOW_POWER -> getAsInteger(keyIndex).toString()
            else -> NULL_VALUE
        }
    }

    override fun getAsFloat(keyIndex: Int): Float {
        if (keyIndex == INDEX_WINDOW_SPEED && windowTimeMillis > 0) {
            return distance * 1000f / windowTimeMillis
        }
        return 0f
    }

    override fun getAsInteger(keyIndex: Int): Int {
        if (keyIndex == INDEX_WINDOW_POWER && windowTimeMillis > 0) {
            return (energy / windowTimeMillis).toInt()
        }
        return 0
    }

    override fun update(point: GpxPointNode, autoPause: Boolean): Boolean {
        if (autoPause)
            return autoPause

        val timeDelta = point.getTimeDelta()
        if (timeDelta <= 0 || timeDelta > MAX_TIME_DELTA)
            return autoPause

        val dist = point.getDistance()

        val attr = point.getAttributes()
        val sampleEnergy = if (attr.hasKey(PowerAttributes.KEY_INDEX_POWER)) {
            attr.getAsFloat(PowerAttributes.KEY_INDEX_POWER).toDouble() * timeDelta
        } else {
            0.0
        }

        windowTimeMillis += timeDelta
        distance += dist
        energy += sampleEnergy

        samples.addLast(Sample(timeDelta, dist, sampleEnergy))
        trim()

        return autoPause
    }

    private fun trim() {
        while (samples.size > 1 && windowTimeMillis > WINDOW_MILLIS) {
            val s = samples.removeFirst()
            windowTimeMillis -= s.timeMillis
            distance -= s.distance
            energy -= s.energy
        }
    }

    companion object {
        private const val WINDOW_MILLIS = 600_000L // 10 minutes

        /**
         * Skip updates when timeDelta is larger than this number of
         * milliseconds.
         */
        private const val MAX_TIME_DELTA = 10_000L

        private val KEYS = Keys()

        @JvmField
        val INDEX_WINDOW_SPEED = KEYS.add("WindowSpeed")

        @JvmField
        val INDEX_WINDOW_POWER = KEYS.add("WindowPower")
    }
}
