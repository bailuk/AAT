package ch.bailu.aat.services.render;

import android.util.SparseArray;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observer;

import java.util.Set;

import ch.bailu.aat.services.cache.MapsForgeTileObject;
import ch.bailu.aat.util.ui.AppLog;

public class Cache implements TileCache {

    private final SparseArray<MapsForgeTileObject> cache = new SparseArray(20);


    @Override
    public boolean containsKey(Job j) {
        // if this returns true MapWorkerPool will remove this RendererJob

        MapsForgeTileObject o = cache.get(toKey(j));

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
        MapsForgeTileObject owner =  cache.get(toKey(job));

        if (owner != null) {
            return owner.getTileBitmap();
        }
        return null;
    }



    /**
     *
     * @param job
     * @param fromRenderer
     *
     * This gets called from the renderer
     */
    @Override
    public void put(Job job, TileBitmap fromRenderer) {
        if (fromRenderer != null) {
            fromRenderer.incrementRefCount();

            MapsForgeTileObject owner =  cache.get(toKey(job));

            if (owner != null) {
                owner.onRendered(fromRenderer);
            }
        }
    }



    public void lockToRenderer(MapsForgeTileObject o) {
        cache.put(toKey(o), o);
    }


    public void freeFromRenderer(MapsForgeTileObject o) {
        cache.remove(toKey(o));
    }


    private int toKey(Tile t) { return t.hashCode();}
    private int toKey(MapsForgeTileObject o) {
        return toKey(o.getTile());
    }
    private int toKey(Job j) {
        return toKey(j.tile);
    }


    @Override
    public void setWorkingSet(Set<Job> workingSet) {}

    @Override
    public void addObserver(Observer observer) {
        AppLog.d(this, "Use lockToRenderer()!");
    }

    @Override
    public void removeObserver(Observer observer) {
        AppLog.d(this, "Use freeFromRenderer()!");
    }
}
