package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.lib.color.AltitudeColorTable
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.elevation.tile.DemProvider
import org.mapsforge.core.model.Tile

class ObjTileElevationColor(id: String, b: MapTileInterface, t: Tile, split: Int) :
    ObjTileElevation(id, b, t, split) {
    override fun fillBuffer(
        bitmap: IntArray,
        raster: Raster,
        span: SubTile,
        demtile: DemProvider
    ) {
        val dim = demtile.dim.DIM
        val bitmapDim = span.pixelDim()

        var c = 0
        var oldLine = -1
        var color = 0

        for (la in span.laSpan.firstPixelIndex()..span.laSpan.lastPixelIndex()) {
            val line = raster.toLaRaster[la] * dim
            var offset = -1

            if (oldLine != line) {
                for (lo in span.loSpan.firstPixelIndex()..span.loSpan.lastPixelIndex()) {
                    val newOffset = raster.toLoRaster[lo]

                    if (newOffset != offset) {
                        offset = newOffset
                        color = AltitudeColorTable.instance()
                            .getColor(demtile.getElevation(line + offset).toInt())
                    }

                    bitmap[c] = color
                    c++
                }
            } else {
                copyLine(bitmap, c - bitmapDim, c)
                c += bitmapDim
            }

            oldLine = line
        }
    }

    private fun copyLine(buffer: IntArray, sourceIndex: Int, destinationIndex: Int) {
        var si = sourceIndex
        var di = destinationIndex
        val nextLine = di

        while (si < nextLine) {
            buffer[di] = buffer[si]
            di++
            si++
        }
    }

    class Factory(private val mapTile: Tile) : Obj.Factory() {
        override fun factory(id: String, appContext: AppContext): Obj {
            return ObjTileElevationColor(id, appContext.createMapTile(), mapTile, SPLIT)
        }

        companion object {
            private const val SPLIT = 0
        }
    }
}
