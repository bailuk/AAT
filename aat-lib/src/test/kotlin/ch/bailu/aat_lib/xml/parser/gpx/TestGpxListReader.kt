package ch.bailu.aat_lib.xml.parser.gpx

import ch.bailu.aat_lib.gpx.attributes.AutoPause
import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.foc_extended.FocResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestGpxListReader {

    @Test
    fun testReader() {
        val reader = GpxListReader(FocResource("test.gpx"), AutoPause.NULL)
        assertEquals(1855, reader.gpxList.pointList.size())

        val attributes = reader.gpxList.getDelta().getAttributes()
        assertEquals(15, attributes.size())

        assertEquals(false, attributes.hasKey(Keys.toIndex("SomeRandomKey")))

        val key = Keys.toIndex("StepRate")
        assertEquals(true, attributes.hasKey(key))
        assertEquals(0, attributes.getAsInteger(key))

        val key2 = Keys.toIndex("TotalSteps")
        assertEquals(true, attributes.hasKey(key2))
        assertEquals(2959, attributes.getAsInteger(key2))

        val key3 = Keys.toIndex("MaxStepsRate")
        assertEquals(true, attributes.hasKey(key3))
        assertEquals(122, attributes.getAsInteger(key3))

    }
}
