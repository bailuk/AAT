package ch.bailu.aat.preferences.location

import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import kotlin.math.roundToLong

class SolidGpsTimeFix(s: StorageInterface) : SolidBoolean(s, SolidGpsTimeFix::class.java.simpleName) {
    override fun getLabel(): String {
        return Res.str().p_fix_gps()
    }

    override fun getToolTip(): String? {
        return if (checked > 0) {
            if (differenceHour == 0L) {
                Res.str().p_fix_correct()
            } else {
                Res.str()
                    .p_fix_differs() + " " + differenceHour
            }
        } else {
            null
        }
    }

    companion object {
        private var checked = 0
        private var differenceHour: Long = 0
        private var differenceMillis: Long = 0

        @JvmStatic
        fun fix(gpsTime: Long, systemTime: Long): Long {
            if (checked < 5) {
                differenceHour = getDifferenceHour(gpsTime, systemTime)
                differenceMillis = getMillisFromHour(differenceHour)
                checked++
            }
            return if (differenceHour == 0L) {
                gpsTime
            } else {
                gpsTime + differenceMillis
            }
        }

        private fun getMillisFromHour(hour: Long): Long {
            return hour * 1000L * 60L * 60L
        }

        private fun getDifferenceHour(gpsTime: Long, systemTime: Long): Long {
            val millis = systemTime - gpsTime
            val seconds = millis / 1000L
            val minutes = seconds / 60.0
            return (minutes / 60.0).roundToLong()
        }
    }
}
