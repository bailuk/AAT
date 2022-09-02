package ch.bailu.aat_lib.map.tile;


import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.TilePosition;
import org.mapsforge.map.model.common.ObservableInterface;
import org.mapsforge.map.model.common.Observer;

import java.util.List;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastData;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.map.Attachable;
import ch.bailu.aat_lib.map.tile.source.Source;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjTile;

public class TileProvider implements Attachable, ObservableInterface {
    private final AppContext appContext;
    private final Source source;

    private TileObjectCache cache = TileObjectCache.NULL;

    private boolean isAttached=false;


    private final Observers observers = new Observers();

    public TileProvider(AppContext sc, Source s) {
        appContext = sc;
        source = s;
    }




    private final BroadcastReceiver onFileChanged = args -> {
        String file =  BroadcastData.getFile(args);

        if (cache.isInCache(file)) observers.notifyChange();
    };



    public synchronized void preload(List<TilePosition> tilePositions) {
        cache.setCapacity(tilePositions.size());

        for (TilePosition p : tilePositions) {
            cache.get(p.tile);
        }

        for (TilePosition p : tilePositions) {
            getHandle(p.tile);
        }
    }



    private synchronized ObjTile getHandle(Tile tile) {
        ObjTile handle = getTileHandle(tile);

        if (handle != null) {
            handle.access();
        }
        return handle;
    }



    public synchronized TileBitmap get(Tile tile) {
        ObjTile handle = getHandle(tile);

        if (handle != null) {
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
        cache.reDownloadTiles(appContext);
    }

    public Source getSource() {
        return source;
    }


    public synchronized boolean isAttached() {
        return isAttached;
    }

    @Override
    public synchronized void onAttached() {
        if (!isAttached) {

            cache.reset();
            cache = new TileObjectCache();

            appContext.getBroadcaster().register(
                    onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);

            isAttached = true;
        }

    }



    @Override
    public synchronized void onDetached() {
        if (isAttached) {
            appContext.getBroadcaster().unregister(onFileChanged);
            cache.reset();
            cache = TileObjectCache.NULL;

            isAttached = false;
        }
    }



    private ObjTile getTileHandle(Tile tile) {
        ObjTile handle = cache.get(tile);


        if (handle == null) {
            handle = getTileHandleLevel2(tile);

            if (handle != null) cache.put(handle);
        }

        return handle;
    }

    private ObjTile getTileHandleLevel2(final Tile mapTile) {
        final ObjTile[] r = {null};

        new InsideContext(appContext.getServices()) {
            @Override
            public void run() {
                String id = source.getID(mapTile, appContext);

                Obj handle = appContext.getServices().getCacheService().getObject(
                        id,
                        source.getFactory(mapTile)
                );


                if (handle instanceof ObjTile) {
                    r[0] = (ObjTile) handle;
                }

            }
        };

        return r[0];
    }



    public synchronized TileBitmap getNonCached(final Tile mapTile) {
        final TileBitmap[] r = {null};

        new InsideContext(appContext.getServices()) {
            @Override
            public void run() {
                String id = source.getID(mapTile, appContext);

                Obj handle = appContext.getServices().getCacheService().getObject(
                        id
                );


                if (handle instanceof ObjTile) {
                    r[0] = ((ObjTile) handle).getTileBitmap();
                    handle.free();
                }

            }
        };

        return r[0];
    }

    public synchronized boolean isLoaded() {
        return cache.isLoaded();
    }

    public synchronized void CheckAllDone() {
        if (isLoaded()) {
            appContext.getBroadcaster().broadcast(AppBroadcaster.CACHE_IS_LOADED);
        }
    }
}
