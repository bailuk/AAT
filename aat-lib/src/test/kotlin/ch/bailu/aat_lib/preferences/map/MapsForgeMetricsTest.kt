package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.map.MapMetrics
import ch.bailu.aat_lib.map.MapsForgeMetrics
import ch.bailu.aat_lib.preferences.map.mock.MockAppDensity
import ch.bailu.aat_lib.preferences.map.mock.MockMapView
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mapsforge.core.model.Dimension

class MapsForgeMetricsTest {

    @Test
    fun fromPixel() {
        val mockMapView = MockMapView()
        val mapMetrics = MapsForgeMetrics(mockMapView, MockAppDensity())

        mockMapView.mockDimension = Dimension(0,0) // Dimension of map view
        val metrics1 = mapMetrics.fromPixel(0,0) // Center of map view
        assertNull(metrics1) // Because dimension of map view is 0

        mockMapView.mockDimension = Dimension(100,100) // Dimension of map view
        val metrics2 = mapMetrics.fromPixel(50,50) // Center of map view
        assertNotNull(metrics2)
        if (metrics2 is MapMetrics) {
            assertEquals(0.0, metrics2.getLatitude(), 0.01)
            assertEquals(0.0, metrics2.getLongitude(), 0.01)
        }
    }
}
