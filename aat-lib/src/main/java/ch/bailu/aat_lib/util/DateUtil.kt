package ch.bailu.aat_lib.util

object DateUtil {
    const val SECONDS_IN_MINUTE: Long = 60
    const val MINUTES_IN_HOUR: Long = 60
    const val HOURS_IN_DAY: Long = 24
    const val DAYS_IN_YEAR: Long = 365
    const val MILLIS_IN_SECOND: Long = 1000

    const val MILLIS_IN_HOUR: Long = MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLIS_IN_SECOND
    const val MILLIS_IN_DAY: Long = MILLIS_IN_HOUR * HOURS_IN_DAY
    const val MILLIS_IN_YEAR: Long = MILLIS_IN_DAY * DAYS_IN_YEAR
}
