package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.foc_extended.FocResource
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestGpxListReaderXml {

    @Test
    fun testReader() {
        val reader = GpxListReaderXml(FocResource("test.gpx"), AutoPause.NULL)
        Assertions.assertEquals(1855, reader.gpxList.pointList.size())

        val attributes = reader.gpxList.getDelta().getAttributes()
        Assertions.assertEquals(15, attributes.size())

        Assertions.assertEquals(false, attributes.hasKey(Keys.toIndex("SomeRandomKey")))

        val key = Keys.toIndex("StepRate")
        Assertions.assertEquals(true, attributes.hasKey(key))
        Assertions.assertEquals(0, attributes.getAsInteger(key))

        val key2 = Keys.toIndex("TotalSteps")
        Assertions.assertEquals(true, attributes.hasKey(key2))
        Assertions.assertEquals(2959, attributes.getAsInteger(key2))

        val key3 = Keys.toIndex("MaxStepsRate")
        Assertions.assertEquals(true, attributes.hasKey(key3))
        Assertions.assertEquals(122, attributes.getAsInteger(key3))

    }
}
