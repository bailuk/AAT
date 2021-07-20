package ch.bailu.aat.map.tile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.TilePosition;
import org.mapsforge.map.model.common.ObservableInterface;
import org.mapsforge.map.model.common.Observer;

import java.util.List;

import ch.bailu.aat_lib.map.Attachable;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.services.cache.ObjTile;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

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



    public synchronized Bitmap get(Tile tile, Resources r) {
        ObjTile handle = getHandle(tile);

        if (handle != null) {
            return handle.getAndroidBitmap();
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

            OldAppBroadcaster.register(scontext.getContext(),
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

        new InsideContext(scontext) {
            @Override
            public void run() {
                String id = source.getID(mapTile, scontext.getContext());

                Obj handle = scontext.getCacheService().getObject(
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

        new InsideContext(scontext) {
            @Override
            public void run() {
                String id = source.getID(mapTile, scontext.getContext());

                Obj handle = scontext.getCacheService().getObject(
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


    public synchronized boolean isReadyAndLoaded() {
        return cache.isReadyAndLoaded();
    }
}
