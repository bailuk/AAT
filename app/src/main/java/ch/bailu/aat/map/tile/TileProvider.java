package ch.bailu.aat.map.tile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.model.common.Observer;

import java.util.ArrayList;

import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;

public class TileProvider implements TileProviderInterface {


    private final ServiceContext scontext;
    private final ArrayList<Observer> observers = new ArrayList(2);
    private final Source source;

    private TileCache<TileObject> cache = TileCache.NULL;

    private boolean isAttached=false;


    public TileProvider(ServiceContext sc, Source s) {
        scontext =sc;
        source = s;
    }


    private final BroadcastReceiver onFileChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String string = AppIntent.getFile(intent);
            if (cache.get((string)) != null) {
                notifyChange();
            }
        }

    };



    @Override
    public synchronized TileBitmap get(Tile tile) {
        TileObject handle = getTileHandle(tile);
        if (handle != null) {
            return handle.getTileBitmap();
        }
        return null;
    }

    @Override
    public synchronized boolean contains(Tile tile) {
        return cache.get(tile) != null;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }


    private void notifyChange() {
        for (Observer o: observers) o.onChange();
    }

    @Override
    public synchronized int getCapacity() {
        return cache.size();
    }

    @Override
    public synchronized void setCapacity(int size) {
        cache.setCapacity(size);
    }



    @Override
    public int getMaximumZoomLevel() {
        return source.getMaximumZoomLevel();
    }

    @Override
    public int getMinimumZoomLevel() {
        return source.getMinimumZoomLevel();
    }

    @Override
    public void reDownloadTiles() {
        cache.reDownloadTiles(scontext);
    }

    @Override
    public Source getSource() {
        return source;
    }


    @Override
    public synchronized void attach() {
        if (isAttached == false) {

            cache.reset();
            cache = new TileObjectCache();

            AppBroadcaster.register(scontext.getContext(),
                    onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);

            isAttached = true;
        }

    }


    @Override
    public synchronized void detach() {
        if (isAttached) {
            scontext.getContext().unregisterReceiver(onFileChanged);
            cache.reset();
            cache = TileCache.NULL;

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

    private TileObject getTileHandleLevel2(Tile mapTile) {
        TileObject r = null;

        if (scontext.lock()) {
            String id = source.getID(mapTile, scontext.getContext());

            ObjectHandle handle = scontext.getCacheService().getObject(
                    id,
                    source.getFactory(mapTile)
            );


            if (handle instanceof TileObject) {
                r = (TileObject) handle;
            }
            scontext.free();
        }
        return r;
    }
}
