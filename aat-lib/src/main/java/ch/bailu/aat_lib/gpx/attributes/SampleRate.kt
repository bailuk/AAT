package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.Keys.Companion.toIndex
import kotlin.math.roundToInt

open class SampleRate private constructor(keys: Keys, private vararg val KEY: Int) :
    GpxSubAttributes(keys) {
    private var sampleTimeMillis = 0L
    private var totalTimeMillis = 0L
    private var totalSamples60KM: Long = 0
    var maxSpm = 0
        private set

    override fun get(keyIndex: Int): String {
        return getAsInteger(keyIndex).toString()
    }

    class StepsRate : SampleRate(KEYS, *GPX_KEYS) {
        override fun getAsInteger(keyIndex: Int): Int {
            return if (keyIndex == INDEX_MAX_SPM) {
                maxSpm
            } else super.getAsInteger(keyIndex)
        }

        companion object {
            @JvmField
            val GPX_KEYS = intArrayOf(
                StepCounterAttributes.KEY_INDEX_STEPS_RATE
            )
            private val KEYS = Keys()
            @JvmField
            val INDEX_MAX_SPM = KEYS.add("MaxStepsRate")
        }
    }

    class HeartRate : SampleRate(KEYS, *GPX_KEYS) {
        override fun getAsInteger(keyIndex: Int): Int {
            if (keyIndex == INDEX_HEART_BEATS) {
                return totalSamples
            } else if (keyIndex == INDEX_AVERAGE_HR) {
                return averageSpm
            } else if (keyIndex == INDEX_MAX_HR) {
                return maxSpm
            }
            return super.getAsInteger(keyIndex)
        }

        companion object {
            @JvmField
            val GPX_KEYS = intArrayOf(
                HeartRateAttributes.KEY_INDEX_BPM,
                toIndex("bpm"),
                toIndex("HR")
            )
            private val KEYS = Keys()
            val INDEX_AVERAGE_HR = KEYS.add("HeartRate")
            @JvmField
            val INDEX_MAX_HR = KEYS.add("MaxHeartRate")
            val INDEX_HEART_BEATS = KEYS.add("HeartBeats")
        }
    }

    class Cadence : SampleRate(KEYS, *GPX_KEYS) {
        override fun getAsInteger(keyIndex: Int): Int {
            if (keyIndex == INDEX_CADENCE) {
                return averageSpm
            } else if (keyIndex == INDEX_TOTAL_CADENCE) {
                return totalSamples
            } else if (keyIndex == INDEX_MAX_CADENCE) {
                return maxSpm
            }
            return super.getAsInteger(keyIndex)
        }

        companion object {
            @JvmField
            val GPX_KEYS = intArrayOf(
                CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM,
                toIndex("rpm")
            )
            private val KEYS = Keys()
            val INDEX_CADENCE = KEYS.add("Cadence")
            @JvmField
            val INDEX_MAX_CADENCE = KEYS.add("MaxCadence")
            val INDEX_TOTAL_CADENCE = KEYS.add("TotalCadence")
        }
    }

    override fun update(point: GpxPointNode, autoPause: Boolean): Boolean {
        if (!autoPause) {
            val attr = point.attributes
            sampleTimeMillis += point.timeDelta
            totalTimeMillis += point.timeDelta
            val spm = getValue(attr, *KEY)
            if (spm > 0) {
                val bpSample60KM = sampleTimeMillis * spm
                totalSamples60KM += bpSample60KM
                sampleTimeMillis = 0L
                if (spm > maxSpm) {
                    maxSpm = spm
                }
            }
        }
        return autoPause
    }

    val totalSamples: Int
        get() = (totalSamples60KM / 60000f).roundToInt()
    val averageSpm: Int
        get() = if (totalTimeMillis > 0) (totalSamples60KM / totalTimeMillis.toDouble()).roundToInt() else 0

    companion object {
        @JvmStatic
        fun getValue(attr: GpxAttributes, vararg keys: Int): Int {
            var r = 0
            for (k in keys) {
                if (attr.hasKey(k)) {
                    r = attr.getAsInteger(k)
                    if (r > 0) {
                        break
                    }
                }
            }
            return r
        }
    }
}
