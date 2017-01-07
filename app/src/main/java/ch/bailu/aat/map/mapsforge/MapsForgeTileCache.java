package ch.bailu.aat.map.mapsforge;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.queue.Job;
import org.mapsforge.map.model.common.Observer;

import java.util.Set;

import ch.bailu.aat.map.Attachable;
import ch.bailu.aat.map.tile.TileProviderInterface;
import ch.bailu.aat.util.ui.AppLog;

public class MapsForgeTileCache  implements TileCache, Attachable {

    private final TileProviderInterface tileProvider;

    public MapsForgeTileCache(TileProviderInterface p) {
        tileProvider=p;
    }


    @Override
    public boolean containsKey(Job job) {
        return tileProvider.contains(job.tile);
    }

    @Override
    public void destroy() {
        tileProvider.onDetached();
    }



    @Override
    public TileBitmap get(Job job) {
        TileBitmap r = tileProvider.get(job.tile);
        if (r != null && !r.isDestroyed()) {
            r.incrementRefCount();

        }
        return  r;
    }




    @Override
    public int getCapacity() {
        return tileProvider.getCapacity();
    }

    @Override
    public int getCapacityFirstLevel() {
        return tileProvider.getCapacity();
    }


    @Override
    public TileBitmap getImmediately(Job job) {
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

        tileProvider.setCapacity(set.size());

        for (Job j: set) {
            if (tileProvider.contains(j.tile)) tileProvider.get(j.tile);
        }
    }


    @Override
    public void addObserver(Observer observer) {
        tileProvider.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        tileProvider.removeObserver(observer);
    }

    @Override
    public void onAttached() {
        tileProvider.onAttached();
    }

    @Override
    public void onDetached() {
        tileProvider.onDetached();
    }
}
