package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.service.cache.ObjTile
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile
import ch.bailu.aat_lib.service.elevation.tile.DemDimension
import ch.bailu.aat_lib.service.elevation.tile.DemGeoToIndex
import ch.bailu.aat_lib.service.elevation.tile.DemProvider
import ch.bailu.aat_lib.service.elevation.tile.DemSplitter
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.core.model.Tile


abstract class ObjTileElevation(id: String, private val bitmap: MapTileInterface, private val mapTile: Tile, private val split: Int)
    : ObjTile(id), ElevationUpdaterClient {

    private val subTiles = SubTiles()
    private val raster = Raster()


    override fun getTileBitmap(): TileBitmap? {
        return bitmap.getTileBitmap()
    }

    override fun getTile(): Tile {
        return mapTile
    }

    fun split(dem: DemProvider): DemProvider {
        var result = dem
        var i = split
        while (i > 0) {
            result = factorySplitter(result)
            i--
        }
        return result
    }

    open fun factorySplitter(dem: DemProvider): DemProvider {
        return DemSplitter.factory(dem)
    }

    open fun factoryGeoToIndex(dim: DemDimension): DemGeoToIndex {
        return DemGeoToIndex(dim)
    }

    private val geoToIndex: DemGeoToIndex
        get() {
            val dim = split(Dem3Tile.NULL).dim
            return factoryGeoToIndex(dim)
        }


    abstract fun fillBuffer(bitmap: IntArray?, raster: Raster?, span: SubTile?, demtile: DemProvider?)

    override fun onInsert(appContext: AppContext) {
        appContext.services.getCacheService().addToBroadcaster(this)

        if (!raster.isInitialized) {
            appContext.services.getBackgroundService().process(RasterInitializer(id))
        }
    }

    override fun onRemove(appContext: AppContext) {
        appContext.services.getElevationService().cancelElevationUpdates(this)

        super.onRemove(appContext)
        bitmap.free()
    }

    override fun onChanged(id: String, appContext: AppContext) {}

    override fun onDownloaded(id: String, url: String, appContext: AppContext) {
        if (subTiles.haveID(url)) {
            requestElevationUpdates(appContext)
        }
    }

    override fun getSize(): Long {
        return bitmap.getSize()
    }

    override fun isReadyAndLoaded(): Boolean { // isDisplayable()
        return this.isInitialized && subTiles.isNotPainting
    }

    override fun isLoaded(): Boolean { // isCacheable()
        return this.isInitialized && subTiles.areAllPainted()
    }

    override fun updateFromSrtmTile(appContext: AppContext, tile: Dem3Tile) {
        appContext.services.getBackgroundService()
            .process(SubTilePainter(appContext.services, id, tile))
    }


    override fun reDownload(sc: AppContext) {}

    open val isInitialized: Boolean
        get() = raster.isInitialized

    fun bgOnProcessPainter(dem3Tile: Dem3Tile): Long {
        var size: Long = 0

        if (this.isInitialized) {
            val subTile = subTiles.take(dem3Tile.coordinates)

            if (subTile != null) {
                size = paintSubTile(subTile, dem3Tile)

                subTiles.done()
            }
        }

        return size
    }


    private fun paintSubTile(subTile: SubTile, dem3Tile: Dem3Tile): Long {
        val interR = subTile.toRect()
        val buffer = IntArray(interR.width() * interR.height())
        fillBuffer(buffer, raster, subTile, split(dem3Tile))

        bitmap.setBuffer(buffer, interR)

        return interR.width().toLong() * interR.height() * 2
    }

    fun bgOnProcessInitializer(a: AppContext): Long {
        raster.initialize(getTile(), geoToIndex, subTiles)
        requestElevationUpdates(a)

        return (SolidTileSize.DEFAULT_TILESIZE * 2).toLong()
    }

    fun requestElevationUpdates(appContext: AppContext) {
        if (this.isInitialized) appContext.services.getElevationService().requestElevationUpdates(
            this, subTiles.toSrtmCoordinates()
        )
    }
}
