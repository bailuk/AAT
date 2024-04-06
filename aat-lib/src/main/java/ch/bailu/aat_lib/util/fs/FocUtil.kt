package ch.bailu.aat_lib.util.fs

import ch.bailu.foc.Foc
import java.io.BufferedInputStream
import java.io.IOException

object FocUtil {
    fun toStr(file: Foc): String {
        return try {
            toString(file)
        } catch (e: IOException) {
            ""
        }
    }

    @Throws(IOException::class)
    fun toString(file: Foc): String {
        val builder = StringBuilder(file.length().toInt() + 1)

        BufferedInputStream(file.openR()).use { inputStream ->
            var b: Int
            while (inputStream.read().also { b = it } > -1) {
                val c = b.toChar()
                builder.append(c)
            }
        }
        return builder.toString()
    }
}
