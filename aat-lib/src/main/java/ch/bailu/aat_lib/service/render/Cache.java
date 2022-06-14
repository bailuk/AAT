package ch.bailu.aat_lib.service.render;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.service.cache.ObjTileMapsForge;

public final class Cache implements TileCache {

    private final Map <Integer, ObjTileMapsForge> cache = new HashMap<>();

    /**
     * Interface function for MapWorkerPool.
     * If this returns true MapWorkerPool will remove this RendererJob
     *
     * @param j job id
     * @return returns true if render job does not exists or exists and is finished
     */
    @Override
    public boolean containsKey(Job j) {
        ObjTileMapsForge o = cache.get(toKey(j));

        return (o == null || o.isLoaded());
    }

    @Override
    public void destroy() {
        purge();
    }

    @Override
    public void purge() {
        cache.clear();
    }

    @Override
    public int getCapacity() {
        return cache.size();
    }

    @Override
    public int getCapacityFirstLevel() {
        return getCapacity();
    }

    @Override
    public TileBitmap getImmediately(Job key) {
        return get(key);
    }

    @Override
    public TileBitmap get(Job job) {
        ObjTileMapsForge owner =  cache.get(toKey(job));

        if (owner != null) {
            return owner.getTileBitmap();
        }
        return null;
    }

    /**
     *
     * This gets called from the renderer
     */
    @Override
    public void put(Job job, TileBitmap fromRenderer) {
        if (fromRenderer != null) {
            fromRenderer.incrementRefCount();

            ObjTileMapsForge owner =  cache.get(toKey(job));

            if (owner != null) {
                owner.onRendered(fromRenderer);
            }
        }
    }

    public void lockToRenderer(ObjTileMapsForge o) {
        cache.put(toKey(o), o);
    }

    public void freeFromRenderer(ObjTileMapsForge o) {
        cache.remove(toKey(o));
    }

    private int toKey(Tile t) { return t.hashCode();}
    private int toKey(ObjTileMapsForge o) {
        return toKey(o.getTile());
    }
    private int toKey(Job j) {
        return toKey(j.tile);
    }

    @Override
    public void setWorkingSet(Set<Job> workingSet) {}

    @Override
    public void addObserver(Observer observer) {
        AppLog.w(this, "Use lockToRenderer()!");
    }

    @Override
    public void removeObserver(Observer observer) {
        AppLog.w(this, "Use freeFromRenderer()!");
    }

    public Collection<ObjTileMapsForge> getTiles() {
        return cache.values();
    }
}
