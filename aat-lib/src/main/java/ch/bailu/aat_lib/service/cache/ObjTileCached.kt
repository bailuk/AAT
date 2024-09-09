package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.map.tile.source.CacheOnlySource
import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.core.model.Tile

class ObjTileCached(id: String, sc: AppContext, private val mapTile: Tile, source: Source) : ObjTile(id) {

    private val cachedFactory: Obj.Factory
    private val sourceFactory: Obj.Factory = source.getFactory(mapTile)
    private val cachedID: String
    private val sourceID: String = source.getID(mapTile, sc)

    private var tile = NULL
    private val save: SaveTileTask

    private val cachedImageFile: Foc

    init {
        val cached: Source = CacheOnlySource(source)

        cachedID = cached.getID(mapTile, sc)
        cachedFactory = cached.getFactory(mapTile)
        cachedImageFile = sc.toFoc(cachedID)

        save = SaveTileTask(sourceID, cachedImageFile)
    }

    override fun onInsert(sc: AppContext) {
        tile = if (isLoadable) {
            sc.services.getCacheService().getObject(cachedID, cachedFactory) as ObjTile
        } else {
            sc.services.getCacheService().getObject(sourceID, sourceFactory) as ObjTile
        }
        sc.services.getCacheService().addToBroadcaster(this)
    }

    private val isLoadable: Boolean
        get() = cachedImageFile.exists()

    override fun onChanged(id: String, sc: AppContext) {
        if (id == tile.toString()) {
            sc.broadcaster.broadcast(
                AppBroadcaster.FILE_CHANGED_INCACHE,
                toString()
            )

            if (mapTile.zoomLevel <= MIN_SAVE_ZOOM_LEVEL && id == sourceID &&
                tile.isLoaded()
            ) {
                sc.services.getBackgroundService().process(save)
            }
        }
    }

    override fun access() {
        tile.access()
        super.access()
    }

    override fun onRemove(cs: AppContext) {
        tile.free()
    }

    override fun getTileBitmap(): TileBitmap? {
        return tile.getTileBitmap()
    }

    override fun getTile(): Tile {
        return mapTile
    }

    override fun reDownload(sc: AppContext) {
        cachedImageFile.rm()

        tile.free()
        tile = sc.services.getCacheService().getObject(sourceID, sourceFactory) as ObjTile
    }

    override fun isLoaded(): Boolean {
        return tile.isLoaded()
    }

    override fun getSize(): Long {
        return MIN_SIZE.toLong()
    }

    override fun onDownloaded(id: String, url: String, sc: AppContext) {}

    override fun getFile(): Foc {
        return cachedImageFile
    }

    class Factory(private val tile: Tile, private val source: Source) : Obj.Factory() {
        override fun factory(id: String, cs: AppContext): Obj {
            return ObjTileCached(id, cs, tile, source)
        }
    }

    companion object {
        private const val MIN_SAVE_ZOOM_LEVEL = 16
    }
}
