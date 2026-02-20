package ch.bailu.aat_lib.util.fs

import ch.bailu.foc.Foc
import java.io.BufferedInputStream
import java.io.IOException
import java.util.Locale

object FileUtil {
    private const val MAX_TRY = 99

    /**
     * Reads file into a string
     * Returns empty string on error
     */
    fun readIntoString(file: Foc): String {
        return try {
            readIntoStringThrows(file)
        } catch (e: IOException) {
            ""
        }
    }

    /**
     * Read contents of file into a string
     * Throws error on error
     */
    @Throws(IOException::class)
    private fun readIntoStringThrows(file: Foc): String {
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

    @Throws(IOException::class)
    fun generateUniqueFilePath(directory: Foc, prefix: String, extension: String): Foc {
        var file = directory.child(generateFileName(prefix, extension))
        var x = 1
        while (file.exists() && x < MAX_TRY) {
            file = directory.child(generateFileName(prefix, x, extension))
            x++
        }
        if (file.exists()) throw IOException()
        return file
    }

    fun generateDatePrefix(): String {
        val time = System.currentTimeMillis()
        return String.format(
            Locale.ROOT,
            "%tY_%tm_%td_%tH_%tM", time, time, time, time, time
        )
    }

    private fun generateFileName(prefix: String, extension: String): String {
        return "${prefix}${toDotExtension(extension)}"
    }

    private fun generateFileName(prefix: String, i: Int, extension: String): String {
        return "${prefix}_$i${toDotExtension(extension)}"
    }

    private fun toDotExtension(extension: String): String {
        return if (extension.startsWith(".")) {
            extension
        } else {
            ".$extension"
        }
    }
}
