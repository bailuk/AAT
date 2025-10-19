package ch.bailu.aat_lib.map.tile.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjTileCacheOnly
import ch.bailu.aat_lib.util.fs.AppDirectory.getTileFile
import org.mapsforge.core.model.Tile

class CacheOnlySource(private val original: Source) : Source() {
    override val name: String
        get() = original.name

    override fun getID(tile: Tile, context: AppContext): String {
        return getTileFile(
            genRelativeFilePath(tile, original.name),
            context.tileCacheDirectory
        ).path
    }

    override val minimumZoomLevel: Int
        get() = original.minimumZoomLevel

    override val maximumZoomLevel: Int
        get() = original.maximumZoomLevel

    override val alpha: Int
        get() = original.alpha


    override fun getFactory(tile: Tile): Obj.Factory {
        return ObjTileCacheOnly.Factory(tile, original)
    }

    override val isTransparent: Boolean
        get() = original.isTransparent
}
