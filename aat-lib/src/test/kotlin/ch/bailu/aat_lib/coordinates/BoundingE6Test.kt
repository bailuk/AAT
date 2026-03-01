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

    @Test
    fun nullBounding() {
        val boundingBoxE6 = BoundingBoxE6.NULL_BOX
        assertEquals(false, boundingBoxE6.hasBounding())

        val center = boundingBoxE6.center
        val boundingBox = boundingBoxE6.toBoundingBox()
        val center1 = boundingBox.centerPoint
        assertEquals(0, center1.latitudeE6)
        assertEquals(0, center1.longitudeE6)
        assertEquals(0.0, center1.latitude)
        assertEquals(0.0, center1.longitude)

        val center2 = center.toLatLong()
        assertEquals(90000000, center2.latitudeE6)
        assertEquals(180000000, center2.longitudeE6)
        assertEquals(90.0, center2.latitude)
        assertEquals(180.0, center2.longitude)
    }
}
