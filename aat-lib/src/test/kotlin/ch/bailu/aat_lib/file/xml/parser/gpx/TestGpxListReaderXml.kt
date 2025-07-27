package ch.bailu.aat_lib.file.xml.parser.gpx

import ch.bailu.aat_lib.gpx.GpxPointNode
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

    @Test
    fun testBrokenGpx() {
        val reader = GpxListReaderXml(FocResource("test-broken.gpx"), AutoPause.NULL)

        val first = reader.gpxList.pointList.first as GpxPointNode
        val second = reader.gpxList.pointList.last as GpxPointNode

        Assertions.assertEquals(2, reader.gpxList.pointList.size())
        Assertions.assertEquals(1711804688000, reader.gpxList.getDelta().getStartTime())

        Assertions.assertEquals(47.791209, first.getLatitude())
        Assertions.assertEquals(7.901156, first.getLongitude())
        Assertions.assertEquals(1711804688000, first.getTimeStamp())
        Assertions.assertEquals(543.7f, first.getAltitude())

        Assertions.assertEquals(47.791209, second.getLatitude())
        Assertions.assertEquals(7.901157, second.getLongitude())
        Assertions.assertEquals(1711801089000, second.getTimeStamp())
        Assertions.assertEquals(544.0f, second.getAltitude())
    }
}
