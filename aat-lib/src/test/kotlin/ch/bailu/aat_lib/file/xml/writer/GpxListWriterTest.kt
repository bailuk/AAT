package ch.bailu.aat_lib.file.xml.writer

import ch.bailu.aat_lib.file.xml.parser.gpx.GpxListReaderXml
import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.mock.MockAppConfig
import ch.bailu.aat_lib.mock.MockFoc
import ch.bailu.foc_extended.FocResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GpxListWriterTest {

    @Test
    fun testGpxListWriter() {
        MockAppConfig.init()

        val mockFoc = MockFoc("only-in-memory.gpx")

        val reader = GpxListReaderXml(FocResource("test.gpx"), AutoPause.NULL)
        assertEquals(1855, reader.gpxList.pointList.size())

        GpxListWriter(reader.gpxList, mockFoc).use { }

        // Should contain everything
        assertEquals(7023, mockFoc.getLineCount())

        val reader2 = GpxListReaderXml(mockFoc, AutoPause.NULL)
        assertEquals(1855, reader2.gpxList.pointList.size())
    }
}
