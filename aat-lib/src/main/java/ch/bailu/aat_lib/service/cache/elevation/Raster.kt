package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.service.cache.Span
import ch.bailu.aat_lib.service.elevation.tile.DemGeoToIndex
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.model.Tile
import org.mapsforge.core.util.MercatorProjection
import kotlin.math.floor


class Raster {
    val toLaRaster: IntArray = IntArray(SolidTileSize.DEFAULT_TILESIZE)
    val toLoRaster: IntArray = IntArray(SolidTileSize.DEFAULT_TILESIZE)

    var isInitialized: Boolean = false
        private set

    /**
     * Initialize subTiles (add all possible tiles to list)
     * @param subTiles Result parameter: Empty list that will be initialized after return
     */
    @Synchronized
    fun initialize(tile: Tile, geoToIndex: DemGeoToIndex, subTiles: SubTiles) {
        if (!this.isInitialized) {
            val laSpan = ArrayList<Span>(5)
            val loSpan = ArrayList<Span>(5)

            initializeWGS84Raster(laSpan, loSpan, tile)
            initializeIndexRaster(geoToIndex)
            subTiles.generateSubTileList(laSpan, loSpan)

            this.isInitialized = true
        }
    }

    // 1. pixel to latitude
    private fun initializeWGS84Raster(laSpan: ArrayList<Span>, loSpan: ArrayList<Span>, tile: Tile) {
        val tileR = getTileR(tile)

        val tl = pixelToGeo(
            tile.zoomLevel,
            tileR.left,
            tileR.top
        )

        val br = pixelToGeo(
            tile.zoomLevel,
            tileR.right,
            tileR.bottom
        )

        val laDiff = (br.latitudeE6 - tl.latitudeE6).toFloat()
        val loDiff = (br.longitudeE6 - tl.longitudeE6).toFloat() //-1.2 - -1.5 = 0.3  //1.5 - 1.2 = 0.3


        var la = tl.latitudeE6.toFloat()
        var lo = tl.longitudeE6.toFloat()

        val laS = Span()
        val loS = Span()

        val laInc = laDiff / (SolidTileSize.DEFAULT_TILESIZE)
        val loInc = loDiff / (SolidTileSize.DEFAULT_TILESIZE)
        var i = 0
        while (i < SolidTileSize.DEFAULT_TILESIZE) {
            toLaRaster[i] = Math.round(la)
            toLoRaster[i] = Math.round(lo)

            val laDeg = floor((la / 1e6f).toDouble()).toInt()
            val loDeg = floor((lo / 1e6f).toDouble()).toInt()

            laS.incrementAndCopyIntoArray(laSpan, i, laDeg)
            loS.incrementAndCopyIntoArray(loSpan, i, loDeg)

            la += laInc
            lo += loInc
            i++
        }

        // flush
        laS.copyIntoArray(laSpan)
        loS.copyIntoArray(loSpan)
    }

    // 2. pixel to dem index
    private fun initializeIndexRaster(toIndex: DemGeoToIndex) {
        for (i in 0 until SolidTileSize.DEFAULT_TILESIZE) {
            toLaRaster[i] = toIndex.toYIndex(toLaRaster[i])
            toLoRaster[i] = toIndex.toXIndex(toLoRaster[i])
        }
    }

    private fun pixelToGeo(z: Byte, x: Int, y: Int): LatLong {
        val mapSize = MercatorProjection.getMapSize(z, SolidTileSize.DEFAULT_TILESIZE)
        return MercatorProjection.fromPixels(x.toDouble(), y.toDouble(), mapSize)
    }

    private fun getTileR(tile: Tile): Rect {
        val r = Rect()
        r.top = tile.tileY * SolidTileSize.DEFAULT_TILESIZE
        r.left = tile.tileX * SolidTileSize.DEFAULT_TILESIZE
        r.right = r.left + SolidTileSize.DEFAULT_TILESIZE - 1
        r.bottom = r.top + SolidTileSize.DEFAULT_TILESIZE - 1
        return r
    }
}
