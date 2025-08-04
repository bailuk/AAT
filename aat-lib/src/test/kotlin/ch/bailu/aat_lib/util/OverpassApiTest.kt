package ch.bailu.aat_lib.util

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.mock.MockAppContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OverpassApiTest {

    @Test
    fun testPreviewURl() {
        val api = object: OverpassApi(MockAppContext()) {
            override val queryString: String
                get() = "[tourism=\"hotel\";]"
        }

        val expected = "https://overpass-api.de/api/interpreter?data=(\n" +
        "node[tourism=\"hotel\"(-90.00,-180.00,90.00,180.00);\n" +
        "rel[tourism=\"hotel\"(-90.00,-180.00,90.00,180.00);\n" +
        "way[tourism=\"hotel\"(-90.00,-180.00,90.00,180.00);\n" +
        "](-90.00,-180.00,90.00,180.00);\n" +
        ">;);out;"
        assertEquals("Overpass", api.apiName)
        assertEquals(expected, api.getUrlPreview("[tourism=\"hotel\";]", BoundingBoxE6()))
    }
}
