package ch.bailu.aat_lib;

import org.junit.jupiter.api.Test;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPoint;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGpxList {

    @Test
    public void testAppend() {
        GpxList gpxList = new GpxList(GpxType.TRACK, GpxListAttributes.factoryTrackList());

        long time = System.currentTimeMillis();

        LatLong latLong = new LatLong(47.623799,8.756476);
        GpxPoint tp = new GpxPoint(latLong, 450, time);
        gpxList.appendToCurrentSegment(tp, GpxListAttributes.factoryTrackList());

        assertEquals(latLong.latitude, ((GpxPointNode)gpxList.getPointList().getLast()).getLatitude());
        assertEquals(latLong.longitude, ((GpxPointNode)gpxList.getPointList().getLast()).getLongitude());
        assertEquals(450, ((GpxPointNode)gpxList.getPointList().getLast()).getAltitude());
        assertEquals(time, ((GpxPointNode)gpxList.getPointList().getLast()).getTimeStamp());

        gpxList.appendToNewSegment(tp, GpxListAttributes.factoryTrackList());
        assertEquals(latLong.latitude, ((GpxPointNode)gpxList.getPointList().getLast()).getLatitude());
        assertEquals(latLong.longitude, ((GpxPointNode)gpxList.getPointList().getLast()).getLongitude());
        assertEquals(450, ((GpxPointNode)gpxList.getPointList().getLast()).getAltitude());
        assertEquals(time, ((GpxPointNode)gpxList.getPointList().getLast()).getTimeStamp());

    }

    @Test
    public void testGpxInformation() {
        GpxList gpxList = new GpxList(GpxType.TRACK, GpxListAttributes.factoryTrackList());

        long time = System.currentTimeMillis();

        LatLong latLong = new LatLong(47.623799,8.756476);
        GpxPoint tp = new GpxPoint(latLong, 450, time);
        gpxList.appendToCurrentSegment(tp, GpxListAttributes.factoryTrackList());

        GpxInformation info = new GpxInformation();
        info.setVisibleTrackPoint(((GpxPointNode)gpxList.getPointList().getLast()));

        assertEquals(latLong.latitude, info.getLatitude());
        assertEquals(latLong.longitude, info.getLongitude());
        assertEquals(450, info.getAltitude());
        assertEquals(time, info.getTimeStamp());

    }
}
