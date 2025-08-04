package ch.bailu.aat_lib.file.json

import ch.bailu.aat_lib.gpx.GpxListIterator
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.foc_extended.FocResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GpxListReaderJsonTest {

    @Test
    fun testGraphHopper() {
        val points = arrayOf(
            47.4053, 8.50512,
            47.40526, 8.50517,
            47.40564, 8.50556,
            47.40578, 8.50568,
            47.40643, 8.50615
        )

        val jsonFile = FocResource("graph-hopper.json")
        testJson(jsonFile, points)
    }

    @Test
    fun testOSRM() {
        val points = arrayOf(
            47.405301, 8.505115,
            47.405321, 8.505086,
            47.406533, 8.506128,
            47.406496, 8.506199,
            47.406427, 8.506148,
        )

        val jsonFile = FocResource("osrm.json")
        testJson(jsonFile, points)
    }

    @Test
    fun testValhalla() {
        val points = arrayOf(
            47.405321, 8.505086,
            47.405259, 8.505174,
            47.405507, 8.505429,
            47.405642, 8.505561,
            47.405707, 8.50562,
            47.405778, 8.505681,
            47.405923, 8.505787,
            47.406216, 8.505992,
            47.406427, 8.506148
        )

        val jsonFile = FocResource("valhalla.json")
        testJson(jsonFile, points)
    }

    private fun testJson(jsonFile: FocResource, expected: Array<Double>) {
        val reader = GpxListReaderJson(jsonFile)
        val result = reader.gpxList

        var index = 0
        val iterator = GpxListIterator(result)
        while (iterator.nextPoint()) {
            assertEquals(expected[index++], iterator.point.getLatitude())
            assertEquals(expected[index++], iterator.point.getLongitude())
        }
        assertEquals(expected.size, index)
        assertEquals(GpxType.ROUTE, result.getDelta().getType())
    }
}
