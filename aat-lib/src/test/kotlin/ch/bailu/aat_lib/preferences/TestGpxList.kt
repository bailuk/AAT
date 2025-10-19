package ch.bailu.aat_lib.preferences

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mapsforge.core.model.LatLong

class TestGpxList {
    @Test
    fun testAppend() {
        val trackList = GpxListAttributes.factoryTrackList()
        val gpxList = GpxList(GpxType.TRACK, trackList)
        val time = System.currentTimeMillis()
        val latLong = LatLong(47.623799, 8.756476)
        val tp = GpxPoint(latLong, 450f, time)
        gpxList.appendToCurrentSegment(tp, GpxListAttributes.factoryTrackList())
        Assertions.assertEquals(
            latLong.latitude,
            (gpxList.pointList.last as GpxPointNode?)!!.getLatitude()
        )
        Assertions.assertEquals(
            latLong.longitude,
            (gpxList.pointList.last as GpxPointNode?)!!.getLongitude()
        )
        Assertions.assertEquals(450f, (gpxList.pointList.last as GpxPointNode?)!!.getAltitude())
        Assertions.assertEquals(time, (gpxList.pointList.last as GpxPointNode?)!!.getTimeStamp())
        gpxList.appendToNewSegment(tp, GpxListAttributes.factoryTrackList())
        Assertions.assertEquals(
            latLong.latitude,
            (gpxList.pointList.last as GpxPointNode?)!!.getLatitude()
        )
        Assertions.assertEquals(
            latLong.longitude,
            (gpxList.pointList.last as GpxPointNode?)!!.getLongitude()
        )
        Assertions.assertEquals(450f, (gpxList.pointList.last as GpxPointNode?)!!.getAltitude())
        Assertions.assertEquals(time, (gpxList.pointList.last as GpxPointNode?)!!.getTimeStamp())
    }

    @Test
    fun testGpxInformation() {
        val gpxList = GpxList(GpxType.TRACK, GpxListAttributes.factoryTrackList())
        val time = System.currentTimeMillis()
        val latLong = LatLong(47.623799, 8.756476)
        val tp = GpxPoint(latLong, 450f, time)
        gpxList.appendToCurrentSegment(tp, GpxListAttributes.factoryTrackList())
        val info = GpxInformation()

        val node = gpxList.pointList.last
        if (node is GpxPointNode) {
            info.setVisibleTrackPoint(node)
        }
        Assertions.assertEquals(latLong.latitude, info.getLatitude())
        Assertions.assertEquals(latLong.longitude, info.getLongitude())
        Assertions.assertEquals(450f, info.getAltitude())
        Assertions.assertEquals(time, info.getTimeStamp())
    }
}
