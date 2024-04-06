package ch.bailu.aat_lib.map.tile

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.cache.LockCache
import ch.bailu.aat_lib.service.cache.ObjTile
import org.mapsforge.core.model.Tile
import java.io.Closeable

open class TileObjectCache : Closeable {
    private val tiles = LockCache<ObjTile>(INITIAL_CAPACITY)

    /**
     * Non synchronized function. This works because LockCache can only grow.
     * @param fileId id (path) of file that might be inside the cache
     * @return true if object corresponding to  fileId is in cache else return false
     */
    fun isInCache(fileId: String?): Boolean {
        if (fileId != null) {
            val hash = fileId.hashCode()
            for (i in 0 until tiles.size()) {
                val o = tiles[i]
                if (o != null && hash == tiles[i].hashCode()) {
                    return true
                }
            }
        }
        return false
    }

    @Synchronized
    operator fun get(string: String): ObjTile? {
        for (i in 0 until tiles.size()) {
            if (tiles[i].toString() == string) {
                return tiles.use(i)
            }
        }
        return null
    }

    /**
     * Get the tile with specific zoom level and coordinates from the cache
     * If it can find the tile inside the cache it sets the access time to now.
     *
     * @param tile Coordinates and zoom level of the requested tile
     * @return A tile from the cache or null if it was not in the cache
     */
    @Synchronized
    operator fun get(tile: Tile): ObjTile? {
        for (i in 0 until tiles.size()) {
            if (compare(tile, tiles[i]!!.tile)) {
                return tiles.use(i)
            }
        }
        return null
    }

    @Synchronized
    open fun put(handle: ObjTile?) {
        tiles.add(handle)
    }

    @Synchronized
    override fun close() {
        tiles.close()
    }

    @Synchronized
    fun reDownloadTiles(sc: AppContext) {
        for (i in 0 until tiles.size()) {
            tiles[i]?.reDownload(sc)
        }
    }

    @Synchronized
    fun reset() {
        tiles.reset()
    }

    @Synchronized
    fun setCapacity(capacity: Int) {
        tiles.ensureCapacity(capacity)
    }

    @Synchronized
    fun size(): Int {
        return tiles.size()
    }

    @get:Synchronized
    val isReadyAndLoaded: Boolean
        get() {
            for (i in 0 until tiles.size()) {
                if (tiles[i] != null) {
                    if (tiles[i]?.isReadyAndLoaded != true) return false
                }
            }
            return true
        }

    companion object {
        private const val INITIAL_CAPACITY = 5
        val NULL: TileObjectCache = object : TileObjectCache() {
            override fun put(handle: ObjTile?) {}
        }

        fun compare(a: Tile, b: Tile): Boolean {
            return a.tileX == b.tileX && a.tileY == b.tileY && a.zoomLevel == b.zoomLevel
        }
    }
}
