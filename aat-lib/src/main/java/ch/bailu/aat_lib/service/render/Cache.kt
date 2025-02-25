package ch.bailu.aat_lib.service.render

import ch.bailu.aat_lib.logger.AppLog.w
import ch.bailu.aat_lib.service.cache.ObjTileMapsForge
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.core.model.Tile
import org.mapsforge.map.layer.cache.TileCache
import org.mapsforge.map.layer.queue.Job
import org.mapsforge.map.model.common.Observer
import java.util.concurrent.ConcurrentHashMap

class Cache : TileCache {
    private val cache: MutableMap<Int, ObjTileMapsForge> = ConcurrentHashMap()

    /**
     * Interface function for MapWorkerPool.
     * If this returns true MapWorkerPool will remove this RendererJob
     *
     * @param j job id
     * @return returns true if render job does not exists or exists and is finished
     */
    override fun containsKey(j: Job): Boolean {
        val o = cache[toKey(j)]

        return (o == null || o.isLoaded())
    }

    override fun destroy() {
        purge()
    }

    override fun purge() {
        cache.clear()
    }

    override fun getCapacity(): Int {
        return cache.size
    }

    override fun getCapacityFirstLevel(): Int {
        return capacity
    }

    override fun getImmediately(key: Job): TileBitmap? {
        return get(key)
    }

    override fun get(job: Job): TileBitmap? {
        val owner = cache[toKey(job)]

        if (owner != null) {
            return owner.getTileBitmap()
        }
        return null
    }

    /**
     *
     * This gets called from the renderer
     */
    override fun put(job: Job, fromRenderer: TileBitmap) {
        fromRenderer.incrementRefCount()
        val owner = cache[toKey(job)]
        owner?.onRendered(fromRenderer)
    }

    fun lockToRenderer(o: ObjTileMapsForge) {
        cache[toKey(o)] = o
    }

    fun freeFromRenderer(o: ObjTileMapsForge) {
        cache.remove(toKey(o))
    }

    private fun toKey(t: Tile): Int {
        return t.hashCode()
    }

    private fun toKey(o: ObjTileMapsForge): Int {
        return toKey(o.getTile())
    }

    private fun toKey(j: Job): Int {
        return toKey(j.tile)
    }

    override fun setWorkingSet(workingSet: Set<Job>) {}

    override fun addObserver(observer: Observer) {
        w(this, "Use lockToRenderer()!")
    }

    override fun removeObserver(observer: Observer) {
        w(this, "Use freeFromRenderer()!")
    }

    val tiles: Collection<ObjTileMapsForge>
        get() = cache.values
}
