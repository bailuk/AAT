package ch.bailu.aat.preferences.map

import ch.bailu.aat_lib.preferences.SolidIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.DateUtil

/**
 * TODO move to lib
 */
class SolidTrimDate(storageInterface: StorageInterface) : SolidIndexList(storageInterface, SolidTrimDate::class.java.simpleName) {
    companion object {
        private class Entry(val value: Long) {
            val name: String = describe(value)

            private fun describe(sizeIn: Long): String {
                var size = sizeIn
                val s: String
                if (size >= DateUtil.MILLIS_IN_YEAR) {
                    size /= DateUtil.MILLIS_IN_YEAR
                    s = if (size == 1L) Res.str()
                        .p_trim_year() else Res.str().p_trim_years()
                } else if (size >= DateUtil.MILLIS_IN_DAY * 30) {
                    size /= (DateUtil.MILLIS_IN_DAY * 30)
                    s = if (size == 1L) Res.str()
                        .p_trim_month() else Res.str().p_trim_months()
                } else {
                    size /= DateUtil.MILLIS_IN_DAY
                    s = Res.str().p_trim_days()
                }
                return "$size $s"
            }
        }

        private val entries = arrayOf(
            Entry(2L * DateUtil.MILLIS_IN_YEAR),
            Entry(1L * DateUtil.MILLIS_IN_YEAR),
            Entry(6L * 30L * DateUtil.MILLIS_IN_DAY),
            Entry(3L * 30L * DateUtil.MILLIS_IN_DAY),
            Entry(2L * 30L * DateUtil.MILLIS_IN_DAY),
            Entry(1L * 30L * DateUtil.MILLIS_IN_DAY),
            Entry(2L * 7L * DateUtil.MILLIS_IN_DAY),
            Entry(1L * 7L * DateUtil.MILLIS_IN_DAY)
        )
    }

    override fun getLabel(): String {
        return Res.str().p_trim_age()
    }

    override fun length(): Int {
        return entries.size
    }

    public override fun getValueAsString(i: Int): String {
        return entries[i].name
    }

    fun getValue(): Long {
        return entries[index].value
    }
}
