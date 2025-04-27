package ch.bailu.aat_lib.file.xml.parser.util

import java.io.IOException

class IntegerScanner : AbsScanner() {
    var integer = 0
        private set

    @Throws(IOException::class)
    override fun scan(stream: Stream) {
        integer = 0
        stream.read()
        stream.skipWhitespace()
        while (true) {
            if (stream.haveDigit()) {
                integer *= 10
                integer += stream.digit
            } else {
                break
            }
            stream.read()
        }
    }
}
