package ch.bailu.aat_lib.xml.parser.util

import java.io.IOException
import java.io.Reader

class SimpleStringReader : Reader() {
    private var string = ""
    private var index = 0

    @Throws(IOException::class)
    fun setString(s: String) {
        string = s
        reset()
    }

    override fun read(): Int {
        if (index < string.length) {
            val c = string[index].code
            index++
            return c
        }
        return -1
    }

    @Throws(IOException::class)
    override fun read(cbuf: CharArray): Int {
        return read(cbuf, 0, cbuf.size)
    }

    override fun skip(n: Long): Long {
        var result = n
        if (index + result >= string.length) {
            result = (string.length - index).toLong()
        }
        index += result.toInt()
        return result
    }

    override fun ready(): Boolean {
        return true
    }

    override fun reset() {
        index = 0
    }

    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        if (index >= string.length) return -1
        var c = 0
        var out = off
        while (c < len && out < cbuf.size) {
            cbuf[off] = string[index]
            if (index + 1 < string.length) {
                index++
                c++
                out++
            } else {
                break
            }
        }
        return c
    }

    override fun close() {}
}
