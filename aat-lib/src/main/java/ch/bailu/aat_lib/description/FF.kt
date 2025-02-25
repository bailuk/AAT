package ch.bailu.aat_lib.description

import java.text.DateFormat
import java.text.DecimalFormat

class FF private constructor() {
    @JvmField
    val localDateTime: DateFormat = DateFormat.getDateTimeInstance(
        DateFormat.LONG, DateFormat.LONG
    )
    val localDateTimeCompact: DateFormat = DateFormat.getDateTimeInstance(
        DateFormat.LONG, DateFormat.SHORT
    )

    @JvmField
    val localDate: DateFormat = DateFormat.getDateInstance()


    @JvmField
    val N = DecimalFormat("0")
    @JvmField
    val N1 = DecimalFormat("0.0")
    @JvmField
    val N2 = DecimalFormat("0.00")
    @JvmField
    val N3 = DecimalFormat("0.000")
    @JvmField
    val N6 = DecimalFormat("0.000000")
    val N3_3 = DecimalFormat("000.000")

    companion object {
        private val F: ThreadLocal<FF> = object : ThreadLocal<FF>() {
            public override fun initialValue(): FF {
                return FF()
            }
        }

        @JvmStatic
        fun f(): FF {
            return F.get()
        }
    }
}
