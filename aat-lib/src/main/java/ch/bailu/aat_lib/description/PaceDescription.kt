package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.general.SolidUnit
import kotlin.math.roundToInt

abstract class PaceDescription(s: StorageInterface) : FloatDescription() {
    private val sunit = SolidUnit(s)
    override fun getUnit(): String {
        return sunit.paceUnit
    }

    override fun getValue(): String {
        val pace = cache * sunit.paceFactor
        return getPaceTimeString(pace)
    }

    private fun getPaceTimeString(pace: Float): String {
        return if (sunit.index == SolidUnit.SI) {
            FF.f().N1.format(pace.toDouble())
        } else format(pace)
    }

    fun speedToPace(speed: Float): Float {
        var pace = 0f
        if (speed != 0f) pace = 1f / speed
        return pace
    }

    companion object {
        private val builder = StringBuilder(6)
        fun format(pace: Float): String {
            synchronized(builder) {
                builder.setLength(0)
                return format(builder, pace).toString()
            }
        }

        fun format(out: StringBuilder, pace: Float): StringBuilder {
            val hours: Int
            var minutes: Int

            // 1. calculate milliseconds to unit
            var seconds: Int = pace.roundToInt()
            minutes = seconds / 60
            hours = minutes / 60

            // 2. cut away values that belong to a higher unit
            seconds -= minutes * 60
            minutes -= hours * 60

            appendValueAndDelimiter(out, minutes)
            appendValue(out, seconds)
            return out
        }

        private fun appendValueAndDelimiter(builder: StringBuilder, value: Int) {
            appendValue(builder, value)
            builder.append(":")
        }

        private fun appendValue(builder: StringBuilder, value: Int) {
            if (value < 10) {
                builder.append("0")
            }
            builder.append(value)
        }
    }
}
