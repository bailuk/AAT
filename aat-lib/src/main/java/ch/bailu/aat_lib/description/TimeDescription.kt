package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.resources.Res

open class TimeDescription : LongDescription() {
    override fun getLabel(): String {
        return Res.str().time()
    }

    override fun getUnit(): String {
        return ""
    }

    override fun getValue(): String {
        return format(cache)
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setCache(info.getTimeDelta())
    }

    companion object {
        private val builder = StringBuilder(10)
        fun format(time: Long): String {
            synchronized(builder) {
                builder.setLength(0)
                return format(builder, time).toString()
            }
        }

        fun format(out: StringBuilder, time: Long): StringBuilder {
            val hours: Int
            var minutes: Int

            // 1. calculate milliseconds to unit
            var seconds: Int = (time / 1000).toInt()
            minutes = seconds / 60
            hours = minutes / 60

            // 2. cut away values that belong to a higher unit
            seconds -= minutes * 60
            minutes -= hours * 60
            appendValueAndDelimiter(out, hours)
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
