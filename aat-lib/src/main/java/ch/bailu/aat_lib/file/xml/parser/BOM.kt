package ch.bailu.aat_lib.file.xml.parser

import ch.bailu.foc.Foc
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader

object BOM {
    private const val BOM_UTF8 = 0xEFBBBF
    private const val BOM_UTF16LE = 0xFFFE
    private const val BOM_UTF32BE = 0x0000FEFF
    private const val BOM_UTF32LE = -0x20000

    @Throws(IOException::class)
    fun hasBOM(reader: Reader): Boolean {
        val x = reader.read()
        return x > -1 && isBOM(x)
    }

    private fun isBOM(`in`: Int): Boolean {
        return `in` == BOM_UTF8 || `in` == BOM_UTF16LE || `in` == BOM_UTF32BE || `in` == BOM_UTF32LE
    }

    @Throws(IOException::class)
    fun open(file: Foc): Reader {
        var reader: Reader = InputStreamReader(file.openR())
        if (!hasBOM(reader)) {
            reader.close()
            reader = InputStreamReader(file.openR())
        }
        return BufferedReader(reader)
    }
}
