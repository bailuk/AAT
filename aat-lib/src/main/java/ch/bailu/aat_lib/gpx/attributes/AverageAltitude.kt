package ch.bailu.aat_lib.gpx.attributes

/**
 * Calculate average altitude from at least [.MIN_SAMPLES] samples and at
 * least [.MIN_DISTANCE] distance.
 * This is used to calculate current slope and total ascend and descend of a track.
 *
 * This is used by [AltitudeDelta]
 */
class AverageAltitude {
    private var next_sample_index = 0
    private var samples = 0
    private var distance = 0f
    private var tAltitude = 0f

    /**
     * Add a sample to the averaged sample.
     * @param alt altitude of sample
     * @param dist distance of sample
     * @return true if limit reached (have a new average altitude sample).
     */
    fun add(alt: Float, dist: Float): Boolean {
        if (next_sample_index == 0) {
            distance = dist
            tAltitude = alt
            samples = 1
        } else {
            distance += dist
            tAltitude += alt
            samples++
        }
        return if ((samples < MIN_SAMPLES || distance < MIN_DISTANCE) && samples < MAX_SAMPLES) {
            next_sample_index++
            false
        } else {
            next_sample_index = 0
            true
        }
    }

    fun getDistance(): Float {
        return distance
    }

    fun getAltitude(): Float {
        val alt = tAltitude.toDouble()
        return if (samples > 0) {
            (alt / samples).toFloat()
        } else {
            0f
        }
    }

    companion object {
        private const val MIN_SAMPLES = 5
        private const val MAX_SAMPLES = 50
        private const val MIN_DISTANCE = 50f
    }
}
