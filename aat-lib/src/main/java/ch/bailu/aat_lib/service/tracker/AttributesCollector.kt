package ch.bailu.aat_lib.service.tracker

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.attributes.CadenceSpeedAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull
import ch.bailu.aat_lib.gpx.attributes.GpxAttributesStatic
import ch.bailu.aat_lib.gpx.attributes.HeartRateAttributes
import ch.bailu.aat_lib.gpx.attributes.PowerAttributes
import ch.bailu.aat_lib.gpx.attributes.StepCounterAttributes
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface

class AttributesCollector {
    private var lastLog = System.currentTimeMillis()


    private val collectors = arrayOf(
        Collector(
            InfoID.HEART_RATE_SENSOR, HeartRateAttributes.KEY_INDEX_BPM,
            SHORT_TIMEOUT
        ),

        Collector(
            InfoID.CADENCE_SENSOR, CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM,
            SHORT_TIMEOUT
        ),

        Collector(
            InfoID.POWER_SENSOR, PowerAttributes.KEY_INDEX_POWER,
            SHORT_TIMEOUT
        ),

        Collector(
            InfoID.STEP_COUNTER_SENSOR, StepCounterAttributes.KEY_INDEX_STEPS_RATE,
            LONG_TIMEOUT
        ),

        StepsTotalCollector()
    )


    fun collect(sensorServiceInterface: SensorServiceInterface): GpxAttributes {
        var attr: GpxAttributes? = null
        val time = System.currentTimeMillis()

        if ((time - lastLog) >= LOG_INTERVAL) {
            lastLog = time

            for (c in collectors) {
                attr = c.collect(sensorServiceInterface, attr, time)
            }
        }

        if (attr == null) attr = GpxAttributesNull.NULL
        return attr
    }


    private open class Collector(
        private val infoID: Int,
        private val keyIndex: Int,
        private val maxAge: Long
    ) {
        private var lastInfo: GpxInformation? = null


        fun collect(
            sensorServiceInterface: SensorServiceInterface,
            target: GpxAttributes?,
            time: Long
        ): GpxAttributes? {
            var target = target
            val source = sensorServiceInterface.getInformationOrNull(infoID)

            if (source != null && source !== lastInfo) {
                lastInfo = source

                target = addAttribute(target, source, keyIndex, time)
            }
            return target
        }


        protected fun addAttribute(
            target: GpxAttributes?,
            source: GpxInformation?,
            keyIndex: Int,
            time: Long
        ): GpxAttributes? {
            var target = target
            if (source != null && (time - source.getTimeStamp()) < maxAge) {
                target = addAttribute(target, source.getAttributes(), keyIndex)
            }
            return target
        }

        protected fun addAttribute(
            target: GpxAttributes?,
            source: GpxAttributes,
            keyIndex: Int
        ): GpxAttributes? {
            var target = target
            if (source.hasKey(keyIndex)) {
                target = addAttributeHaveKey(getTargetNotNull(target), source, keyIndex)
            }
            return target
        }

        protected open fun addAttributeHaveKey(
            target: GpxAttributes,
            source: GpxAttributes,
            keyIndex: Int
        ): GpxAttributes? {
            val value = source[keyIndex]

            if (value.isNotEmpty()) {
                target.put(keyIndex, value)
            }
            return target
        }

        protected fun getTargetNotNull(target: GpxAttributes?): GpxAttributes {
            if (target == null) return GpxAttributesStatic()
            return target
        }
    }


    private inner class StepsTotalCollector : Collector(
        InfoID.STEP_COUNTER_SENSOR,
        StepCounterAttributes.KEY_INDEX_STEPS_TOTAL, LONG_TIMEOUT
    ) {
        private var base = -1

        override fun addAttributeHaveKey(
            target: GpxAttributes, source: GpxAttributes,
            keyIndex: Int
        ): GpxAttributes {
            var value = source.getAsInteger(keyIndex)

            if (base == -1) base = value

            value -= base

            target.put(keyIndex, value.toString())
            return target
        }
    }

    companion object {
        private const val LOG_INTERVAL: Long = 0
        private const val SHORT_TIMEOUT = (2 * 1000).toLong()
        private const val LONG_TIMEOUT = (10 * 1000).toLong()
    }
}
