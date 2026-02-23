package ch.bailu.aat_lib.util

import ch.bailu.aat_lib.coordinates.LatLongE6
import ch.bailu.aat_lib.gpx.tools.SimplifierBearing
import ch.bailu.aat_lib.mock.MockAppConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class BearingTest {
    /**
     * Compared with results from
     * [...](https://rechneronline.de/geo-coordinates/#distance)
     * [...](https://www.igismap.com/map-tool/bearing-angle)
     */
    @Test
    fun testBearing() {
        val bearing = 96.51f

        val kansasCity = LatLongE6(39.099912, -94.581213)
        val stLouis = LatLongE6(38.627089, -90.200203)

        Assertions.assertEquals(bearing,
            SimplifierBearing.Companion.getBearing(kansasCity, stLouis), 0.9f)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            MockAppConfig.init()
        }
    }
}
