package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.tile.MapTileInterface
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.core.model.Tile

abstract class ObjTile(id: String) : Obj(id) {
    abstract fun getTileBitmap(): TileBitmap?
    abstract fun getTile(): Tile
    abstract fun reDownload(sc: AppContext)
    abstract override fun isLoaded(): Boolean

    companion object {
        @JvmStatic
        protected fun getSize(bitmap: MapTileInterface, defaultSize: Long): Long {
            val size = bitmap.getSize()
            return if (size == 0L) {
                defaultSize
            } else {
                size
            }
        }

        @JvmField
        val NULL: ObjTile = object : ObjTile("") {
            override fun getTileBitmap(): TileBitmap? {
                return null
            }

            override fun getTile(): Tile {
                return Tile(5,5,5,5)
            }

            override fun reDownload(sc: AppContext) {}

            override fun isLoaded(): Boolean {
                return false
            }

            override fun getSize(): Long {
                return MIN_SIZE.toLong()
            }

            override fun onDownloaded(id: String, url: String, sc: AppContext) {}
            override fun onChanged(id: String, sc: AppContext) {}
        }
    }
}
