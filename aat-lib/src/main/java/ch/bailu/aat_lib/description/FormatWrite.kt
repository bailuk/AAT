package ch.bailu.aat_lib.description

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class FormatWrite private constructor() {
    @JvmField
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT)
    @JvmField
    val decimal1 = DecimalFormat("0.0", DecimalFormatSymbols(Locale.ROOT))
    @JvmField
    val decimal6 = DecimalFormat("0.000000", DecimalFormatSymbols(Locale.ROOT))

    init {
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    companion object {
        private val F: ThreadLocal<FormatWrite> = object : ThreadLocal<FormatWrite>() {
            public override fun initialValue(): FormatWrite {
                return FormatWrite()
            }
        }

        @JvmStatic
        fun f(): FormatWrite {
            return F.get()
        }
    }
}
