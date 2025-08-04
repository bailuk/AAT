package ch.bailu.aat_lib.coordinates

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BoundingE6Test {

    @Test
    fun testMapsForgeConversion() {
        val boundingBoxE6 = BoundingBoxE6(48300000, 11200000, 45600000, 5000000)
        val boundingBox = boundingBoxE6.toBoundingBox()

        assertEquals(boundingBoxE6.center, BoundingBoxE6(boundingBox).center)
        assertEquals(boundingBoxE6, BoundingBoxE6(boundingBox))
        assertEquals(boundingBoxE6.toBoundingBox(), boundingBox)
        assertEquals(boundingBoxE6.toBoundingBox().centerPoint, boundingBox.centerPoint)
    }
}
