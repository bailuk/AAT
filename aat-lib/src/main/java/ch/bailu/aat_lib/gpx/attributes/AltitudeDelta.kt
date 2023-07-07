package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.GpxPointNode
import kotlin.math.roundToInt

abstract class AltitudeDelta : GpxSubAttributes(KEYS) {
    abstract fun add(alt: Float, dist: Float)
    abstract fun getDescend(): Short
    abstract fun getAscend(): Short
    abstract fun getSlope(): Short
    override fun update(point: GpxPointNode, autoPause: Boolean): Boolean {
        if (!autoPause) add(point.altitude.toFloat(), point.distance)
        return autoPause
    }

    override fun get(keyIndex: Int): String {
        if (keyIndex == INDEX_SLOPE) {
            return getSlope().toString()
        } else if (keyIndex == INDEX_DESCEND) {
            return getDescend().toString()
        } else if (keyIndex == INDEX_ASCEND) {
            return getAscend().toString()
        }
        return NULL_VALUE
    }

    override fun getAsFloat(keyIndex: Int): Float {
        if (keyIndex == INDEX_ASCEND) return getAscend().toFloat() else if (keyIndex == INDEX_DESCEND) return getDescend().toFloat()
        return super.getAsFloat(keyIndex)
    }

    override fun getAsInteger(keyIndex: Int): Int {
        if (keyIndex == INDEX_SLOPE) {
            return getSlope().toInt()
        } else if (keyIndex == INDEX_DESCEND) {
            return getDescend().toInt()
        } else if (keyIndex == INDEX_ASCEND) {
            return getAscend().toInt()
        }
        return super.getAsInteger(keyIndex)
    }

    class LastAverage : AltitudeDelta() {
        private var ascend = 0f
        private var descend = 0f
        private val average = AverageAltitude()
        private var averageB = 0f
        private var distance = 0f
        private var delta = 0f
        private var samples = 0
        override fun add(alt: Float, dist: Float) {
            if (average.add(alt, dist)) {
                val averageA = averageB
                averageB = average.getAltitude()
                distance = average.getDistance()
                if (samples > 0) {
                    delta = averageB - averageA
                    if (delta < 0) descend -= delta else ascend += delta
                }
                samples++
            }
        }

        override fun getAscend(): Short {
            return ascend.toInt().toShort()
        }

        override fun getDescend(): Short {
            return descend.toInt().toShort()
        }

        override fun getSlope(): Short {
            return if (distance > 1) {
                (100 * delta / distance).roundToInt().toShort()
            } else 0
        }
    }

    companion object {
        private val KEYS = Keys()
        val INDEX_SLOPE = KEYS.add("Slope")
        val INDEX_DESCEND = KEYS.add("Descend")
        val INDEX_ASCEND = KEYS.add("Ascend")
    }
}
