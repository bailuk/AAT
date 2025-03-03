package ch.bailu.aat_lib.util

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.mock.MockAppContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NominatimApiTest {

    @Test
    fun testPreviewURl() {
        val api = object: NominatimApi(MockAppContext()) {
            override val queryString: String
                get() = "Basel"
        }

        val expected = "https://nominatim.openstreetmap.org/search?q=Basel&format=xml&bounded=1&viewbox=180.0,-90.0,-180.0,90.0"

        assertEquals("Nominatim", api.apiName)
        assertEquals(expected, api.getUrlPreview("Basel", BoundingBoxE6()))
    }
}
