package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.aat_lib.util.Objects
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.core.model.Tile

open class ObjTileCacheOnly(id: String, sc: AppContext, private val tile: Tile, s: Source) :
    ObjTile(id) {
    private val file: Foc
    private val bitmap: MapTileInterface
    private val source: Source

    init {
        file = sc.toFoc(id)
        source = s
        bitmap = sc.createMapTile()
        sc.services.cacheService.addToBroadcaster(this)
    }

    override fun getTileBitmap(): TileBitmap? {
        return bitmap.getTileBitmap()
    }

    override fun getTile(): Tile {
        return tile
    }

    override fun reDownload(sc: AppContext) {}
    override fun onInsert(sc: AppContext) {
        if (isLoadable) load(sc.services)
    }

    override fun onRemove(sc: AppContext) {
        super.onRemove(sc)
        bitmap.free()
    }

    override fun isLoaded(): Boolean {
        return bitmap.isLoaded()
    }

    protected val isLoadable: Boolean
        get() {
            file.update()
            return file.isFile && file.canRead()
        }

    override fun onDownloaded(id: String, u: String, sc: AppContext) {
        if (Objects.equals(id, getID()) && isLoadable) {
            load(sc.services)
        }
    }

    protected fun fileExists(): Boolean {
        file.update()
        return getFile().exists()
    }

    protected fun load(sc: ServicesInterface) {
        sc.backgroundService.process(TileLoaderTask(file))
    }

    override fun isReadyAndLoaded(): Boolean {
        val loaded = isLoaded
        val notLoadable = !isLoadable
        return loaded || notLoadable
    }

    override fun onChanged(id: String, sc: AppContext) {}
    override fun getSize(): Long {
        return bitmap.getSize()
    }

    override fun getFile(): Foc {
        return file
    }

    private class TileLoaderTask(f: Foc?) : FileTask(f) {
        override fun bgOnProcess(appContext: AppContext): Long {
            val size = longArrayOf(0)
            object : OnObject(appContext, file.toString(), ObjTileCacheOnly::class.java) {
                override fun run(handle: Obj) {
                    val tile = handle as ObjTileCacheOnly
                    tile.bitmap[file, SolidTileSize.DEFAULT_TILESIZE] = tile.source.isTransparent
                    appContext.broadcaster.broadcast(
                        AppBroadcaster.FILE_CHANGED_INCACHE,
                        file.toString()
                    )
                    size[0] = tile.bitmap.getSize()
                }
            }
            return size[0]
        }
    }

    class Factory(private val tile: Tile, private val source: Source) : Obj.Factory() {
        override fun factory(id: String, cs: AppContext): Obj {
            return ObjTileCacheOnly(id, cs, tile, source)
        }
    }
}
