package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observer;
import org.osmdroid.tileprovider.MapTile;

import java.util.ArrayList;
import java.util.Set;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.TileStackObject;
import ch.bailu.aat.map.osmdroid.DynTileProvider;

public class MapsForgeTileCache extends DynTileProvider implements TileCache {

    final private ArrayList<Observer> observers = new ArrayList<Observer>(2);

    private int capacity;

    public MapsForgeTileCache(ServiceContext sc) {
        super(sc);
    }

    @Override
    public void onCacheChanged() {
        for(Observer o: observers) o.onChange();
    }

    @Override
    public boolean containsKey(Job job) {
        return false;
    }

    @Override
    public void destroy() {
        AppLog.d(this, "destroy()");
        observers.clear();
        onDetached();
    }



    @Override
    public TileBitmap get(Job job) {
        //AppLog.d(this, "get()");
        TileStackObject tileStackObject = getMapTileStack(convert(job.tile));

        if (tileStackObject != null) {

            TileBitmap r = tileStackObject.getTileBitmap();
            if (r != null && !r.isDestroyed()) {
                r.incrementRefCount();
                return  r;
            }
        }
        return null;
    }


    public static MapTile convert(Tile tile) {
        //AppLog.d(tile, "Size: " +tile.tileSize);
        return new MapTile(tile.zoomLevel, tile.tileX, tile.tileY);
    }


    @Override
    public int getCapacity() {
        AppLog.d(this, "getCapacity()");
        return capacity;
    }

    @Override
    public int getCapacityFirstLevel() {
        AppLog.d(this, "getCapacityFirstLevel()");
        return 50;
    }


    @Override
    public TileBitmap getImmediately(Job job) {
        //AppLog.d(this, "getImmediately()");
        return get(job);
    }


    @Override
    public void purge() {
        AppLog.d(this, "purge()");
    }

    @Override
    public void put(Job job, TileBitmap tileBitmap) {
        AppLog.d(this, "put()");
    }

    @Override
    public void setWorkingSet(Set<Job> set) {
        //AppLog.d(this, "setWorkingSet()");
        if (set.size()*2 > capacity) {
            capacity = set.size()*2;
            ensureCapacity(set.size());
        }

        for (Job j: set) {
            get(j);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        AppLog.d(this, "lockToCache()");
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        AppLog.d(this, "freeFromCache()");
        observers.remove(observer);
    }


}
