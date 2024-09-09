package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.core.model.Tile

class ObjTileMapsForge(
    id: String,
    private val appContext: AppContext,
    private val tile: Tile,
    @JvmField val themeID: String
) : ObjTile(id) {
    private val bitmap = appContext.createMapTile()

    init {
        appContext.services.getRenderService().lockToRenderer(this)
    }

    override fun reDownload(sc: AppContext) {}

    override fun isLoaded(): Boolean {
        return bitmap.isLoaded()
    }

    override fun onDownloaded(id: String, url: String, sc: AppContext) {}

    override fun onChanged(id: String, sc: AppContext) {}


    fun onRendered(fromRenderer: TileBitmap?) {
        bitmap.set(fromRenderer)
        appContext.broadcaster.broadcast(
            AppBroadcaster.FILE_CHANGED_INCACHE,
            id
        )
    }


    override fun onRemove(sc: AppContext) {
        appContext.services.getRenderService().freeFromRenderer(this@ObjTileMapsForge)
        bitmap.free()
        super.onRemove(sc)
    }


    override fun getSize(): Long {
        DEFAULT_SIZE = getSize(bitmap, DEFAULT_SIZE)

        return if (isLoaded) {
            DEFAULT_SIZE
        } else {
            DEFAULT_SIZE * 4
        }
    }

    override fun getTileBitmap(): TileBitmap? {
        return bitmap.getTileBitmap()
    }

    override fun getTile(): Tile {
        return tile
    }


    class Factory(private val mapTile: Tile, private val themeID: String) : Obj.Factory() {
        override fun factory(id: String, appContext: AppContext): Obj {
            return ObjTileMapsForge(id, appContext, mapTile, themeID)
        }
    }

    companion object {
        private var DEFAULT_SIZE = (SolidTileSize.DEFAULT_TILESIZE_BYTES * 4).toLong()
    }
}
