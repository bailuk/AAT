package ch.bailu.aat_lib.file.xml.parser.util

import java.io.IOException

class DoubleScanner(private val baseExponent: Int) : AbsScanner() {
    var int = 0

    @Throws(IOException::class)
    override fun scan(stream: Stream) {
        var haveDecimal = false
        var negative = false
        var exponent = baseExponent
        var fraction = 0
        stream.read()
        stream.skipWhitespace()
        if (stream.haveA('-'.code)) {
            negative = true
            stream.read()
        }
        while (true) {
            if (stream.haveDigit()) {
                if (haveDecimal) {
                    if (exponent > 0) {
                        fraction *= 10
                        fraction += stream.digit
                        exponent--
                    }
                } else {
                    fraction *= 10
                    fraction += stream.digit
                }
            } else if (stream.haveA('.'.code)) {
                haveDecimal = true
            } else {
                break
            }
            stream.read()
        }
        if (negative) fraction = 0 - fraction
        int = fraction * exp_table[exponent] //Math.pow(10, exponent);
    }

    companion object {
        private val exp_table = intArrayOf(
            1,
            10,
            100,
            1000,
            10000,
            100000,
            1000000,
            10000000,
            100000000
        )
    }
}
