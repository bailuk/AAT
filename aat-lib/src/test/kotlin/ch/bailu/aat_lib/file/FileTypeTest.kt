package ch.bailu.aat_lib.file

import ch.bailu.foc_extended.FocResource
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

class FileTypeTest {

    @Test
    fun testFileType() {
        val type1 = FileType(FocResource("graph-hopper.json"))
        assertFalse(type1.isXML)
        assertTrue(type1.isJSON)

        val type2 = FileType(FocResource("osrm.json"))
        assertFalse(type2.isXML)
        assertTrue(type2.isJSON)

        val type3 = FileType(FocResource("valhalla.json"))
        assertFalse(type3.isXML)
        assertTrue(type3.isJSON)

        val type4 = FileType(FocResource("test.gpx"))
        assertTrue(type4.isXML)
        assertFalse(type4.isJSON)

        try {
            FileType(FocResource("non-existing"))
            assertTrue(false)
        } catch (e: IOException) {
            assertTrue(true)
        }
    }
}
