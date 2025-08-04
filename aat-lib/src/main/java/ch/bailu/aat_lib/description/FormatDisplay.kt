package ch.bailu.aat_lib.description

import java.text.DateFormat
import java.text.DecimalFormat

class FormatDisplay private constructor() {
    val localDateTime: DateFormat = DateFormat.getDateTimeInstance(
        DateFormat.LONG, DateFormat.LONG
    )
    val localDateTimeCompact: DateFormat = DateFormat.getDateTimeInstance(
        DateFormat.LONG, DateFormat.SHORT
    )

    val localDate: DateFormat = DateFormat.getDateInstance()

    val decimal0 = DecimalFormat("0")
    val decimal1 = DecimalFormat("0.0")
    val decimal2 = DecimalFormat("0.00")
    val decimal3 = DecimalFormat("0.000")
    val decimal6 = DecimalFormat("0.000000")
    val decimal3_3 = DecimalFormat("000.000")

    companion object {
        private val F: ThreadLocal<FormatDisplay> = object : ThreadLocal<FormatDisplay>() {
            public override fun initialValue(): FormatDisplay {
                return FormatDisplay()
            }
        }

        fun f(): FormatDisplay {
            return F.get()
        }
    }
}
