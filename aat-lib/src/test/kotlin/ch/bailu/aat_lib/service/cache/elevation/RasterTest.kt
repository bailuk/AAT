package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.service.elevation.tile.Dem3Array
import ch.bailu.aat_lib.service.elevation.tile.DemGeoToIndex
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mapsforge.core.model.Tile

class RasterTest {

    @Test
    fun test() {
        for (x in 1021..1026 ) {
            for (y in 731..738) {
                testTile(x, y, 11)
            }
        }
    }

    private fun testTile(x: Int, y: Int, z: Int) {
        val tile = Tile(x,y,z.toByte(), 256)

        val raster = Raster()
        assertFalse(raster.isInitialized)

        val subTiles = SubTiles()
        raster.initialize(tile, DemGeoToIndex(Dem3Array.dem3Dimension), subTiles)
        assertTrue(raster.isInitialized)

        assertTrue(subTiles.toSrtmCoordinates().isNotEmpty())

    }
}
