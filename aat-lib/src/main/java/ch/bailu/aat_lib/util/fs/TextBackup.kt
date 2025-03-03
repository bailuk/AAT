package ch.bailu.aat_lib.util.fs

import ch.bailu.aat_lib.xml.parser.util.Stream
import ch.bailu.foc.Foc
import java.io.IOException
import java.io.OutputStreamWriter

class TextBackup(private val file: Foc) {
    companion object {
        const val MAX_FILE_SIZE = 200

        @JvmStatic
        @Throws(IOException::class)
        fun write(file: Foc, text: String) {
            TextBackup(file).write(text)
        }

        @JvmStatic
        @Throws(IOException::class)
        fun read(file: Foc): String {
            return TextBackup(file).read()
        }
    }

    @Throws(IOException::class)
    fun write(text: String) {
        OutputStreamWriter(file.openW()).use {
            it.write(text)
        }
    }

    @Throws(IOException::class)
    fun read(): String {
        val buffer = StringBuilder()
        readToBuffer(buffer)
        return buffer.toString()
    }

    @Throws(IOException::class)
    private fun readToBuffer(buffer: StringBuilder) {
        Stream(file).use {
            var count = MAX_FILE_SIZE
            while (count > -1) {
                count--
                it.read()
                if (it.haveEOF()) {
                    break
                } else {
                    buffer.append(it.get().toChar())
                }
            }
        }
    }
}
