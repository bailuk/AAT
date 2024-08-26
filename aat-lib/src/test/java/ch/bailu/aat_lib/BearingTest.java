package ch.bailu.aat_lib;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.gpx.tools.SimplifierBearing;


public class BearingTest {

    @BeforeAll
    public static void init() {
        CoordinatesTest.init();
    }


    /**
     * Compared with results from
     * https://rechneronline.de/geo-coordinates/#distance
     * https://www.igismap.com/map-tool/bearing-angle
     */
    @Test
    public void testBearing() {
        final float bearing = 96.51f;

        LatLongE6 kansasCity = new LatLongE6(39.099912, -94.581213);
        LatLongE6 stLouis = new LatLongE6(38.627089, -90.200203);

        assertEquals (bearing, SimplifierBearing.getBearing(kansasCity, stLouis),0.9f);
    }
}
