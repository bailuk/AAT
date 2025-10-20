package ch.bailu.aat_lib.file.xml.writer

import ch.bailu.aat_lib.file.xml.parser.gpx.GpxListReaderXml
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.mock.MockAppConfig
import ch.bailu.aat_lib.mock.MockFoc
import ch.bailu.foc_extended.FocResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GpxListWriterTest {

    @Test
    fun testGpxListWriter() {
        MockAppConfig.init()

        val mockFoc = MockFoc("only-in-memory.gpx")

        val reader = GpxListReaderXml(FocResource("test.gpx"), AutoPause.NULL)
        assertEquals(1855, reader.gpxList.pointList.size())

        // Write and then close file
        GpxListWriter(reader.gpxList, mockFoc).use { }

        val linesWritten = mockFoc.getLines()

        // Should contain everything
        assertEquals(7023, linesWritten.size)

        val reader2 = GpxListReaderXml(mockFoc, AutoPause.NULL)
        assertEquals(1855, reader2.gpxList.pointList.size())

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>", linesWritten[0])

        // Compare with original file
        FocResource("test.gpx").openR().bufferedReader().useLines { lines->
            lines.forEachIndexed { index, s ->
                if (index == 2) {
                    assertEquals("    creator=\"AAT AAT Activity Tracker v1.29\" version=\"1.1\"", linesWritten[index])
                } else if (index == 5) {
                    // Time stamp is from "now" when file gets written
                    assertTrue(linesWritten[index].startsWith("<metadata><time>"))
                    assertTrue(linesWritten[index].endsWith("</time></metadata>"))
                } else {
                    assertEquals(s, linesWritten[index])
                }
            }
        }
    }
}
