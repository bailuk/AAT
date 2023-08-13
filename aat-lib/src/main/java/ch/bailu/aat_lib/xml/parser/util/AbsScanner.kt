package ch.bailu.aat_lib.xml.parser.util

import java.io.IOException

abstract class AbsScanner {
    private val stringReader = SimpleStringReader()
    private val stringStream = Stream(stringReader)
    @Throws(IOException::class)

    abstract fun scan(stream: Stream)

    @Throws(IOException::class)
    fun scan(string: String) {
        stringReader.setString(string)
        scan(stringStream)
    }
}
