package ch.bailu.aat_lib.lib.json.decoder

/**
 * Decodes a polyline-string and returns each coordinate by calling a callback-function.
 * Does not convert coordinates to decimal.
 *
 * https://developers.google.com/maps/documentation/utilities/polylinealgorithm
 * https://stackoverflow.com/questions/9341020/how-to-decode-googles-polyline-algorithm
 */
class PolylineDecoder(private val encoded: String, onHavePoint: (la: Int, lo: Int) -> Unit) {
    private var index = 0
    private val len = encoded.length

    init {
        var la = 0
        var lo = 0

        while (index < len) {
            la += decodeDelta()
            lo += decodeDelta()
            onHavePoint(la, lo)
        }
    }

    private fun decodeDelta(): Int {
        var result = 0
        var carriageQ = 0
        var isLast = false

        while (index < len && !isLast) {
            var code = encoded[index++].code
            code -= 63
            val bits5 = code shl (32 - 5) ushr (32 - 5)
            result = result or (bits5 shl carriageQ)
            carriageQ += 5
            isLast = (code and (1 shl 5)) == 0
        }

        val isNegative = (result and 1) == 1
        result = result ushr 1
        if (isNegative) {
            result = result.inv()
        }
        return result
    }
}
