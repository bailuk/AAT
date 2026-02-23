package ch.bailu.aat_lib.util

import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.roundToLong

object MemSize {
    const val KB: Long = 1024
    const val MB: Long = 1024 * KB
    const val GB: Long = 1024 * MB

    val dec: DecimalFormat = DecimalFormat("0.00")
    val ldivider: LongArray = longArrayOf(1, KB, MB, GB)
    val ddivider: DoubleArray = doubleArrayOf(1.0, KB.toDouble(), MB.toDouble(), GB.toDouble())
    val unit: Array<String?> = arrayOf("B", "K", "M", "G")

    fun describe(out: StringBuilder, size: Double): StringBuilder {
        var i = ddivider.size

        while (i > 0) {
            i--
            if (abs(size) >= ddivider[i]) break
        }
        out.append(dec.format(size / ddivider[i]))
        out.append(unit[i])
        return out
    }

    fun describe(out: StringBuilder, size: Long): String {
        var i = ldivider.size

        while (i > 0) {
            i--
            if (abs(size) >= ldivider[i]) break
        }

        out.append(size / ldivider[i])
        out.append(unit[i])
        return out.toString()
    }

    fun round(size: Long): Long {
        var size = size
        var i = ldivider.size

        while (i > 0) {
            i--
            if (abs(size) >= ldivider[i]) break
        }

        size = (size / ldivider[i].toDouble()).roundToLong()
        return size * ldivider[i]
    }
}
