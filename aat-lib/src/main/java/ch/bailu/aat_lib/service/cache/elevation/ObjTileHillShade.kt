package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.elevation.tile.DemDimension
import ch.bailu.aat_lib.service.elevation.tile.DemGeoToIndex
import ch.bailu.aat_lib.service.elevation.tile.DemProvider
import ch.bailu.aat_lib.service.elevation.tile.DemSplitter
import ch.bailu.aat_lib.service.elevation.tile.MultiCell
import ch.bailu.aat_lib.service.elevation.tile.MultiCell8
import org.mapsforge.core.model.Tile

class ObjTileHillShade(id: String, ti: MapTileInterface, t: Tile) : ObjTileElevation(id, ti, t, splitFromZoom(t.zoomLevel.toInt())) {
    private var table: ObjHillShadeColorTable? = null

    override fun onInsert(appContext: AppContext) {
        table = appContext.services.getCacheService().getObject(
            ObjHillShadeColorTable.ID,
            ObjHillShadeColorTable.FACTORY
        ) as ObjHillShadeColorTable

        super.onInsert(appContext)
    }


    override val isInitialized: Boolean
        get() = table!!.isReadyAndLoaded() && super.isInitialized


    override fun onChanged(id: String, appContext: AppContext) {
        if (ObjHillShadeColorTable.ID == id) {
            requestElevationUpdates(appContext)
        }
    }

    override fun onRemove(appContext: AppContext) {
        super.onRemove(appContext)
        table?.free()
    }


    override fun factoryGeoToIndex(dim: DemDimension): DemGeoToIndex {
        return DemGeoToIndex(dim, true)
    }


    override fun factorySplitter(dem: DemProvider): DemProvider {
        return DemSplitter(dem)
    }

    private fun factoryMultiCell(dem: DemProvider): MultiCell {
        //return MultiCell.factory(dem);
        return MultiCell8(dem)
    }


    override fun fillBuffer(
        bitmap: IntArray,
        raster: Raster,
        subTile: SubTile,
        demtile: DemProvider
    ) {
        val demtileDim = demtile.dim.DIM
        val bitmapDim = subTile.pixelDim()

        var color = 0
        var index = 0
        var oldLine = -1

        val multiCell = factoryMultiCell(demtile)

        for (la in subTile.laSpan.firstPixelIndex()..subTile.laSpan.lastPixelIndex()) {
            val line = raster.toLaRaster[la] * demtileDim

            if (oldLine != line) {
                var oldOffset = -1

                for (lo in subTile.loSpan.firstPixelIndex()..subTile.loSpan.lastPixelIndex()) {
                    val offset = raster.toLoRaster[lo]

                    if (oldOffset != offset) {
                        oldOffset = offset

                        multiCell.set(line + offset)
                        color = table!!.getColor(multiCell)
                    }

                    bitmap[index] = color
                    index++
                }
            } else {
                copyLine(bitmap, index - bitmapDim, index)
                index += bitmapDim
            }

            oldLine = line
        }
    }

    private fun copyLine(buffer: IntArray, cs: Int, cd: Int) {
        var cs = cs
        var cd = cd
        val nextLine = cd

        while (cs < nextLine) {
            buffer[cd] = buffer[cs]
            cd++
            cs++
        }
    }


    class Factory(private val mapTile: Tile) : Obj.Factory() {
        override fun factory(id: String, appContext: AppContext): Obj {
            return ObjTileHillShade(id, appContext.createMapTile(), mapTile)
        }
    }

    companion object {
        private fun splitFromZoom(zoom: Int): Int {
            var split = 0
            if (zoom > 11) {
                split++
            }
            return split
        }
    }
}
