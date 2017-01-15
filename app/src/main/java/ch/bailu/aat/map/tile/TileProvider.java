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

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppLog;

public class TileProvider implements TileProviderInterface {

    private final ServiceContext scontext;
    private final ArrayList<Observer> observers = new ArrayList(2);
    private final TileObject.Source source;

    private TileCache<TileObject> cache = TileObjectCache.NULL;


    public TileProvider(ServiceContext sc, TileObject.Source s) {
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
    public synchronized Drawable getDrawable(Tile tile) {
        TileBitmap bitmap = get(tile);

        if (bitmap != null)
            return new BitmapDrawable(scontext.getContext().getResources(),
                    AndroidGraphicFactory.getBitmap(bitmap));

        return null;
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
    public synchronized void onAttached() {
        cache.reset();
        cache = new TileObjectCache();
        AppBroadcaster.register(scontext.getContext(),
                onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);

    }

    @Override
    public synchronized void onDetached() {
        AppLog.d(this, "onDetached()");

        scontext.getContext().unregisterReceiver(onFileChanged);
        cache.reset();
        cache = TileObjectCache.NULL;
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
        if (scontext.isUp()) {
            String id = source.getID(mapTile, scontext.getContext());


            ObjectHandle handle = scontext.getCacheService().getObject(
                    id,
                    source.getFactory(mapTile)
            );


            if (handle instanceof TileObject) {
                return (TileObject) handle;
            }
        }
        return null;
    }
}
