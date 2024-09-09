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
        buffer: IntArray,
        raster: Raster,
        subTile: SubTile,
        dem: DemProvider
    ) {
        val dim = dem.dim.DIM
        val bitmapDim = subTile.pixelDim()

        var c = 0
        var oldLine = -1
        var color = 0

        for (la in subTile.laSpan.firstPixelIndex()..subTile.laSpan.lastPixelIndex()) {
            val line = raster.toLaRaster[la] * dim
            var offset = -1

            if (oldLine != line) {
                for (lo in subTile.loSpan.firstPixelIndex()..subTile.loSpan.lastPixelIndex()) {
                    val newOffset = raster.toLoRaster[lo]

                    if (newOffset != offset) {
                        offset = newOffset
                        color = AltitudeColorTable.instance()
                            .getColor(dem.getElevation(line + offset).toInt())
                    }

                    buffer[c] = color
                    c++
                }
            } else {
                copyLine(buffer, c - bitmapDim, c)
                c += bitmapDim
            }

            oldLine = line
        }
    }

    private fun copyLine(buffer: IntArray, sourceIndex: Int, destinationIndex: Int) {
        var sourceIndex = sourceIndex
        var destinationIndex = destinationIndex
        val nextLine = destinationIndex

        while (sourceIndex < nextLine) {
            buffer[destinationIndex] = buffer[sourceIndex]
            destinationIndex++
            sourceIndex++
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
