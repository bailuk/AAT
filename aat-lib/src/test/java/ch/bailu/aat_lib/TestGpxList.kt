package ch.bailu.aat_lib

import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes.Companion.factoryTrackList
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mapsforge.core.model.LatLong

class TestGpxList {
    @Test
    fun testAppend() {
        val trackList = factoryTrackList()
        val gpxList = GpxList(GpxType.TRACK, trackList)
        val time = System.currentTimeMillis()
        val latLong = LatLong(47.623799, 8.756476)
        val tp = GpxPoint(latLong, 450f, time)

        gpxList.appendToCurrentSegment(tp, factoryTrackList())

        val last = gpxList.pointList.last
        Assertions.assertEquals(true, last is GpxPointNode)

        if (last is GpxPointNode) {
            Assertions.assertEquals(
                latLong.latitude,
                last.getLatitude()
            )
            Assertions.assertEquals(
                latLong.longitude,
                last.getLongitude()
            )
            Assertions.assertEquals(
                450.0,
                last.getAltitude()
            )
            Assertions.assertEquals(
                time,
                last.getTimeStamp()
            )
            gpxList.appendToNewSegment(tp, factoryTrackList())
            Assertions.assertEquals(
                latLong.latitude,
                last.getLatitude()
            )
            Assertions.assertEquals(
                latLong.longitude,
                last.getLongitude()
            )
            Assertions.assertEquals(
                450.0,
                last.getAltitude()
            )
            Assertions.assertEquals(
                time,
                last.getTimeStamp()
            )
        }
    }

    @Test
    fun testGpxInformation() {
        val gpxList = GpxList(GpxType.TRACK, factoryTrackList())
        val time = System.currentTimeMillis()
        val latLong = LatLong(47.623799, 8.756476)
        val tp = GpxPoint(latLong, 450f, time)
        gpxList.appendToCurrentSegment(tp, factoryTrackList())
        val info = GpxInformation()

        val last = gpxList.pointList.last
        if (last is GpxPointNode) {
            info.setVisibleTrackPoint(last)
        }
        Assertions.assertEquals(latLong.latitude, info.getLatitude())
        Assertions.assertEquals(latLong.longitude, info.getLongitude())
        Assertions.assertEquals(450.0, info.getAltitude())
        Assertions.assertEquals(time, info.getTimeStamp())
    }
}
