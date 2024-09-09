package ch.bailu.aat_lib.map.tile

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastData.getFile
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.aat_lib.service.cache.ObjTile
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.core.model.Tile
import org.mapsforge.map.layer.TilePosition
import org.mapsforge.map.model.common.ObservableInterface
import org.mapsforge.map.model.common.Observer

class TileProvider(private val appContext: AppContext, val source: Source) : Attachable,
    ObservableInterface {
    private var cache = TileObjectCache.NULL

    @get:Synchronized
    var isAttached = false
        private set
    private val observers = Observers()

    private val onFileChanged = BroadcastReceiver { args: Array<out String> ->
        val file = getFile(args)
        if (cache.isInCache(file)) observers.notifyChange()
    }

    @Synchronized
    fun preload(tilePositions: List<TilePosition>) {
        cache.setCapacity(tilePositions.size)
        for (p in tilePositions) {
            cache[p.tile]
        }
        for (p in tilePositions) {
            getHandle(p.tile)
        }
    }

    /**
     * Get a tile from cache if it is there.
     * If tile is not already inside the cache it loads the tile and
     * puts it into the tile cache.
     *
     * @param tile coordinates and zoom level of tile
     * @return the requested tile or null on failure.
     */
    @Synchronized
    private fun getHandle(tile: Tile): ObjTile? {
        val handle = getTileHandle(tile)
        handle?.access()
        return handle
    }

    @Synchronized
    operator fun get(tile: Tile): TileBitmap? {
        val handle = getHandle(tile)
        return handle?.tileBitmap
    }

    @Synchronized
    operator fun contains(tile: Tile): Boolean {
        return cache[tile] != null
    }

    override fun addObserver(observer: Observer) {
        observers.addObserver(observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.removeObserver(observer)
    }

    val maximumZoomLevel: Int
        get() = source.maximumZoomLevel
    val minimumZoomLevel: Int
        get() = source.minimumZoomLevel

    @Synchronized
    fun reDownloadTiles() {
        cache.reDownloadTiles(appContext)
    }

    @Synchronized
    override fun onAttached() {
        if (!isAttached) {
            cache.reset()
            cache = TileObjectCache()
            appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onFileChanged)
            isAttached = true
        }
    }

    @Synchronized
    override fun onDetached() {
        if (isAttached) {
            appContext.broadcaster.unregister(onFileChanged)
            cache.reset()
            cache = TileObjectCache.NULL
            isAttached = false
        }
    }

    private fun getTileHandle(tile: Tile): ObjTile? {
        var handle = cache[tile]
        if (handle == null) {
            handle = getTileHandleLevel2(tile)
            if (handle != null) cache.put(handle)
        }
        return handle
    }

    private fun getTileHandleLevel2(mapTile: Tile): ObjTile? {
        var result: ObjTile? = null

        appContext.services.insideContext {
            val id = source.getID(mapTile, appContext)
            val handle = appContext.services.getCacheService().getObject(
                id,
                source.getFactory(mapTile)
            )
            if (handle is ObjTile) {
                result = handle
            }
        }
        return result
    }

    @get:Synchronized
    val isReadyAndLoaded: Boolean
        get() = cache.isReadyAndLoaded
}
