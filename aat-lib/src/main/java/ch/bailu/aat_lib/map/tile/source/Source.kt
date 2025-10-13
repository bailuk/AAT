package ch.bailu.aat_lib.map.tile.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.cache.Obj
import org.mapsforge.core.model.Tile

abstract class Source {
    abstract val name: String

    abstract fun getID(aTile: Tile, context: AppContext): String

    abstract val minimumZoomLevel: Int
    abstract val maximumZoomLevel: Int

    abstract val isTransparent: Boolean
    abstract val alpha: Int

    abstract fun getFactory(tile: Tile): Obj.Factory


    open fun filterBitmap(): Boolean {
        return false
    }

    companion object {
        const val EXT: String = ".png"

        const val TRANSPARENT: Int = 150
        const val OPAQUE: Int = 255

        fun genRelativeFilePath(tile: Tile, name: String): String {
            return genID(tile, name) + EXT
        }

        fun genID(t: Tile, name: String): String {
            return name + "/" + t.zoomLevel + "/" + t.tileX + "/" + t.tileY
        }
    }
}
