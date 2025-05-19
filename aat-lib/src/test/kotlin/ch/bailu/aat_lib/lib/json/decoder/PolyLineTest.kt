package ch.bailu.aat_lib.lib.json.decoder

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PolyLineTest {

    @Test
    fun testPolyLine() {

        val las = arrayOf(38.5, 40.7, 43.252)
        val los = arrayOf(-120.2, -120.95, -126.453)

        var index = 0
        PolylineDecoder("_p~iF~ps|U_ulLnnqC_mqNvxq`@") { la, lo ->
            Assertions.assertEquals(las[index], la / 1e5)
            Assertions.assertEquals(los[index++], lo / 1e5)
        }
        Assertions.assertEquals(3, index)
    }
}
