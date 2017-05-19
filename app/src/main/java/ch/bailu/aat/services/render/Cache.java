package ch.bailu.aat.services.render;

import android.util.SparseArray;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observer;

import java.util.Set;

import ch.bailu.aat.services.cache.MapsForgeTileObject;
import ch.bailu.aat.util.ui.AppLog;

public class Cache implements TileCache {
    private static class Entry {
        public final MapsForgeTileObject observer;
        public TileBitmap bitmap = null;

        public Entry(MapsForgeTileObject o) {
            observer = o;
        }
    }

    private final SparseArray<Entry> cache = new SparseArray(20);


    @Override
    public boolean containsKey(Job key) {
        return get(key) != null;
    }

    @Override
    public void destroy() {
        purge();
    }

    @Override
    public TileBitmap get(Job key) {
        Entry e = cache.get(toKey(key));
        if (e != null)
            return e.bitmap;
        return null;
    }


    @Override
    public int getCapacity() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.size()==0;
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
    public void purge() {
        AppLog.d(this, "Purge " + cache.size() + " files. (FIXME)");
        for (int i = 0; i< cache.size(); i++) {
            Entry e = cache.valueAt(i);
            if (e != null && e.bitmap != null) {
                e.bitmap.decrementRefCount();
            }
        }
        cache.clear();
    }

    @Override
    public void put(Job key, TileBitmap bitmap) {
        if (bitmap != null) {
            Entry e =  cache.get(toKey(key));

            if (e != null) {
                bitmap.incrementRefCount();

                if (e.bitmap != null) {
                    AppLog.d(this, "bitmap is NOT NULL (FIXME: needs decrementRefcount?) ");
                }
                e.bitmap = bitmap;

                e.observer.onChange();
            }
        }
    }



    public void lockToCache(MapsForgeTileObject t) {
        cache.put(toKey(t), new Entry(t));
    }


    public void freeFromCache(MapsForgeTileObject t) {
        Entry e = cache.get(toKey(t));

        cache.remove(toKey(t));

        if (e != null && e.bitmap != null) {
            e.bitmap.decrementRefCount();
        }
    }


    private int toKey(MapsForgeTileObject t) {
        return t.getTile().hashCode();
    }


    private int toKey(Job j) {
        return j.tile.hashCode();
    }


    @Override
    public void setWorkingSet(Set<Job> workingSet) {}

    @Override
    public void addObserver(Observer observer) {
        AppLog.d(this, "Use lockToCache()!");
    }

    @Override
    public void removeObserver(Observer observer) {
        AppLog.d(this, "Use freeFromCache()!");
    }
}
