package ch.bailu.aat_lib.description

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class Format private constructor() {
    @JvmField
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT)
    @JvmField
    val decimal1 = DecimalFormat("0", DecimalFormatSymbols(Locale.ROOT))
    @JvmField
    val decimal2 = DecimalFormat("0.0", DecimalFormatSymbols(Locale.ROOT))
    @JvmField
    val decimal6 = DecimalFormat("0.000000", DecimalFormatSymbols(Locale.ROOT))

    init {
        val UTC = TimeZone.getTimeZone("UTC")
        dateFormat.timeZone = UTC
    }

    companion object {
        private val F: ThreadLocal<Format> = object : ThreadLocal<Format>() {
            public override fun initialValue(): Format {
                return Format()
            }
        }

        @JvmStatic
        fun f(): Format {
            return F.get()
        }
    }
}
