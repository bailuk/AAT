package ch.bailu.aat.map.tile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.TilePosition;
import org.mapsforge.map.model.common.ObservableInterface;
import org.mapsforge.map.model.common.Observer;

import java.util.List;

import ch.bailu.aat.map.Attachable;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;

public class TileProvider implements Attachable, ObservableInterface {
    private final ServiceContext scontext;
    private final Source source;

    private TileObjectCache cache = TileObjectCache.NULL;

    private boolean isAttached=false;


    private final Observers observers = new Observers();

    public TileProvider(ServiceContext sc, Source s) {
        scontext = sc;
        source = s;
    }




    private final BroadcastReceiver onFileChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String file =  AppIntent.getFile(intent);

            if (cache.isInCache(file)) observers.notifyChange();
        }

    };



    public synchronized void preload(List<TilePosition> tilePositions) {
        cache.setCapacity(tilePositions.size());

        for (TilePosition p : tilePositions) {
            cache.get(p.tile);
        }

        for (TilePosition p : tilePositions) {
            get(p.tile);
        }
    }


    public synchronized TileBitmap get(Tile tile) {
        TileObject handle = getTileHandle(tile);

        if (handle != null) {
            handle.access();
            return handle.getTileBitmap();
        }
        return null;
    }


    public synchronized boolean contains(Tile tile) {
        return cache.get(tile) != null;
    }

    public void addObserver(Observer observer) {
        observers.addObserver(observer);
    }
    public void removeObserver(Observer observer) {
        observers.removeObserver(observer);
    }



    public int getMaximumZoomLevel() {
        return source.getMaximumZoomLevel();
    }
    public int getMinimumZoomLevel() {
        return source.getMinimumZoomLevel();
    }

    public synchronized void reDownloadTiles() {
        cache.reDownloadTiles(scontext);
    }

    public Source getSource() {
        return source;
    }


    public synchronized boolean isAttached() {
        return isAttached;
    }

    @Override
    public synchronized void onAttached() {
        if (isAttached == false) {

            cache.reset();
            cache = new TileObjectCache();

            AppBroadcaster.register(scontext.getContext(),
                    onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);

            isAttached = true;
        }

    }



    @Override
    public synchronized void onDetached() {
        if (isAttached) {
            scontext.getContext().unregisterReceiver(onFileChanged);
            cache.reset();
            cache = TileObjectCache.NULL;

            isAttached = false;
        }
    }



    private TileObject getTileHandle(Tile tile) {
        TileObject handle = cache.get(tile);


        if (handle == null) {
            handle = getTileHandleLevel2(tile);

            if (handle != null) cache.put(handle);
        }

        return handle;
    }

    private TileObject getTileHandleLevel2(final Tile mapTile) {
        final TileObject[] r = {null};

        new InsideContext(scontext) {
            @Override
            public void run() {
                String id = source.getID(mapTile, scontext.getContext());

                ObjectHandle handle = scontext.getCacheService().getObject(
                        id,
                        source.getFactory(mapTile)
                );


                if (handle instanceof TileObject) {
                    r[0] = (TileObject) handle;
                }

            }
        };

        return r[0];
    }


    public synchronized boolean isReadyAndLoaded() {
        return cache.isReadyAndLoaded();
    }
}
