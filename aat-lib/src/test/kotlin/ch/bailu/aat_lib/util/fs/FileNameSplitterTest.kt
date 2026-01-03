package ch.bailu.aat_lib.util.fs

import ch.bailu.foc.FocName
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FileNameSplitterTest {

    @Test
    fun testFileNameSplitter() {
        FileNameSplitter(FocName("test")).apply {
            assertEquals("", extension)
            assertEquals("", dotExtension)
            assertEquals("test", prefix)
        }


        FileNameSplitter(FocName("test.gpx")).apply {
            assertEquals("gpx", extension)
            assertEquals(".gpx", dotExtension)
            assertEquals("test", prefix)
        }

        FileNameSplitter(FocName("test.gpx.osm")).apply {
            assertEquals("osm", extension)
            assertEquals(".osm", dotExtension)
            assertEquals("test.gpx", prefix)
        }

        FileNameSplitter(FocName(".test")).apply {
            assertEquals("", extension)
            assertEquals("", dotExtension)
            assertEquals("test", prefix)
        }

        FileNameSplitter(FocName(".test.a.b.c.d")).apply {
            assertEquals("d", extension)
            assertEquals(".d", dotExtension)
            assertEquals("test.a.b.c", prefix)
        }

        FileNameSplitter(FocName(".test.a.b.c.d.")).apply {
            assertEquals("d", extension)
            assertEquals(".d", dotExtension)
            assertEquals("test.a.b.c", prefix)
        }

        FileNameSplitter(FocName(".")).apply {
            assertEquals("", extension)
            assertEquals("", dotExtension)
            assertEquals(".", prefix)
        }

    }
}
