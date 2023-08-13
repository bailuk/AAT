package ch.bailu.aat_lib.description

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class FF_GPX private constructor() {
    @JvmField
    val TIME: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT)
    @JvmField
    val N = DecimalFormat("0", DecimalFormatSymbols(Locale.ROOT))
    @JvmField
    val N1 = DecimalFormat("0.0", DecimalFormatSymbols(Locale.ROOT))
    @JvmField
    val N6 = DecimalFormat("0.000000", DecimalFormatSymbols(Locale.ROOT))

    init {
        val UTC = TimeZone.getTimeZone("UTC")
        TIME.timeZone = UTC
    }

    companion object {
        private val F: ThreadLocal<FF_GPX> = object : ThreadLocal<FF_GPX>() {
            public override fun initialValue(): FF_GPX {
                return FF_GPX()
            }
        }

        @JvmStatic
        fun f(): FF_GPX {
            return F.get()
        }
    }
}
