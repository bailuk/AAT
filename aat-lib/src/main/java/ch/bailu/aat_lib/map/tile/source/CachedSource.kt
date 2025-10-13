package ch.bailu.aat_lib.map.tile.source

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjTileCached
import org.mapsforge.core.model.Tile

class CachedSource(private val source: Source) : Source() {
    override val name: String
        get() = "Cached" + source.name

    override fun getID(tile: Tile, context: AppContext): String {
        return "Cached" + source.getID(tile, context)
    }

    override val minimumZoomLevel: Int
        get() = source.minimumZoomLevel

    override val maximumZoomLevel: Int
        get() = source.maximumZoomLevel

    override val alpha: Int
        get() = source.alpha


    override fun getFactory(tile: Tile): Obj.Factory {
        return ObjTileCached.Factory(tile, source)
    }

    override val isTransparent: Boolean
        get() = source.isTransparent


    companion object {
        val CACHED_ELEVATION_HILLSHADE: CachedSource =
            CachedSource(ElevationSource.ELEVATION_HILLSHADE)
    }
}
