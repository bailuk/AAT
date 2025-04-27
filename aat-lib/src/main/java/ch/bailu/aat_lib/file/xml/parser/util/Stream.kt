package ch.bailu.aat_lib.file.xml.parser.util

import ch.bailu.foc.Foc
import java.io.BufferedReader
import java.io.Closeable
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringReader

class Stream : Closeable {
    private val reader: Reader
    private var c = 0

    constructor(foc: Foc) {
        val istream = foc.openR()
        val ireader: Reader = InputStreamReader(istream, CHARSET)
        reader = BufferedReader(ireader, BUFFER_BYTES)
    }

    constructor(r: Reader) {
        reader = r
    }

    constructor(string: String) {
        reader = StringReader(string)
    }

    fun get(): Int {
        return c
    }

    @Throws(IOException::class)
    fun skip(n: Long) {
        reader.skip(n)
    }

    @Throws(IOException::class)
    fun read() {
        c = reader.read()
    }

    @Throws(IOException::class)
    fun read(n: Long) {
        skip(n)
        read()
    }

    fun haveA(x: Int): Boolean {
        return c == x
    }


    @Throws(IOException::class)
    fun skipWhitespace() {
        while (c == ' '.code || c == '\n'.code || c == '\r'.code || c == '\t'.code) read()
    }

    fun haveDigit(): Boolean {
        return c >= '0'.code && c <= '9'.code
    }

    fun haveCharacter(): Boolean {
        return c >= 'A'.code && c <= 'z'.code
    }

    val digit: Int
        get() = c - '0'.code

    fun haveEOF(): Boolean {
        return c == -1
    }

    @Throws(IOException::class)
    fun to(x: Int) {
        do {
            read()
        } while (!haveA(x) && !haveEOF())
    }

    @Throws(IOException::class)
    override fun close() {
        reader.close()
    }

    companion object {
        private const val CHARSET = "UTF-8"
        private const val BUFFER_BYTES = 1024 * 10
    }
}
