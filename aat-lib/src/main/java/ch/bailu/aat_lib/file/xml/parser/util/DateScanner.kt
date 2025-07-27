package ch.bailu.aat_lib.file.xml.parser.util

import java.io.IOException
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone

class DateScanner(var timeMillis: Long) : AbsScanner() {
    private var localOffsetMillis = 0L
    private val minute: IntegerScanner = IntegerScanner()
    private val hour: IntegerScanner = IntegerScanner()
    private val seconds: DoubleScanner = DoubleScanner(3)
    private var utcDateMillis = 0L
    private val dateBuffer = IntArray(10)
    private val localTimeZone: TimeZone = TimeZone.getTimeZone(Calendar.getInstance().timeZone.id)
    private val utcDate: Calendar = GregorianCalendar()


    @Throws(IOException::class)
    override fun scan(stream: Stream) {
        var dateNeedsRescan = false
        stream.read()
        stream.skipWhitespace()
        for (i in dateBuffer.indices) {
            if (dateBuffer[i] != stream.get()) {
                dateBuffer[i] = stream.get()
                dateNeedsRescan = true
            }
            stream.read()
        }
        if (dateNeedsRescan) scanDate()
        if (stream.haveA('T'.code)) scanTime(stream)
        if (!stream.haveA('Z'.code)) {
            timeMillis -= localOffsetMillis
        }
    }

    private fun scanDate() {
        val list = IntArray(3)
        var x = 0
        for (aDateBuffer in dateBuffer) {
            if (aDateBuffer == '-'.code) {
                x++
            } else {
                list[x] *= 10
                list[x] += aDateBuffer - '0'.code
            }
        }
        utcDate.clear()
        utcDate.timeZone = UTC
        utcDate[list[0], list[1] - 1] = list[2] // year, month (zero-based), day
        utcDateMillis = utcDate.timeInMillis
        timeMillis = utcDateMillis
        localOffsetMillis = localTimeZone.getOffset(utcDateMillis).toLong()
    }

    @Throws(IOException::class)
    private fun scanTime(stream: Stream) {
        hour.scan(stream)
        minute.scan(stream)
        seconds.scan(stream)
        timeMillis = seconds.value.toLong()
        timeMillis += (minute.integer * 60 * 1000).toLong()
        timeMillis += (hour.integer * 60 * 60 * 1000).toLong()
        timeMillis += utcDateMillis
    }

    companion object {
        // Localtime to UTC fix
        private val UTC = TimeZone.getTimeZone("UTC")
    }
}
