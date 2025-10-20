package ch.bailu.aat_lib.file

import ch.bailu.foc_extended.FocResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class FileTypeTest {

    @Test
    fun testFileType() {
        assertType("graph-hopper.json", false, true)
        assertType("osrm.json", false, true)
        assertType("valhalla.json", false, true)
        assertType("test.gpx", true, false)
        assertType("test-cm.json", false, true)

        try {
            FileType(FocResource("non-existing"))
            assertTrue(false)
        } catch (e: IOException) {
            assertTrue(true)
        }
    }

    private fun assertType(resourcePath: String, isXML: Boolean, isJSON: Boolean) {
        val type = FileType(FocResource(resourcePath))
        assertEquals(isJSON, type.isJSON)
        assertEquals(isXML, type.isXML)
    }
}
