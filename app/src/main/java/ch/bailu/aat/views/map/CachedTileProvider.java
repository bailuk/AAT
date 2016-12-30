package ch.bailu.aat.views.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import org.mapsforge.core.model.Tile;
import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.services.cache.TileStackObject;

public class CachedTileProvider extends AbsOsmTileProvider {
    private Handler handler = new Handler();
    private TileCache cache = TileCache.NULL;

    private final Context context;
    private final ServiceContext scontext;


    private final BroadcastReceiver onFileChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String string = AppIntent.getFile(intent);
            if (cache.get((string)) != null) {
                handler.sendEmptyMessage(MapTile.MAPTILE_SUCCESS_ID);
            }
        }

    };




    public CachedTileProvider(ServiceContext sc)  {
        super(sc);
        context = sc.getContext();
        scontext = sc;

    }


    public TileStackObject getMapTileStack(MapTile tile, int size) {
        TileStackObject handle = cache.get(tile);


        if (handle == null) {
            handle = getTileHandle(tile, size);

            cache.put(handle);
        }

        if (handle != null) {
            //AppLog.d(this, handle.toString());
            if (handle.getTileBitmap() == null) AppLog.d(this, "not yet created");
            else if (handle.getTileBitmap().isDestroyed()) AppLog.d(this, "is destroyed");
        }
        return handle;
    }




    @Override
    public Drawable getMapTile(MapTile tile) {
        return getMapTileStack(tile, 256).getDrawable(context.getResources());
    }


    @Override
    public void attach() {
        AppBroadcaster.register(context, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
        cache = new LockTileCache();
    }


    @Override
    public void detach() {

        context.unregisterReceiver(onFileChanged);
        cache.close();
        cache = TileCache.NULL;
    }



    @Override
    public void setTileRequestCompleteHandler(Handler h) {
        handler = h;
    }


    @Override
    public void ensureCapacity(int capacity) {
        cache.setCapacity(capacity);
    }


    @Override
    public void setSubTileSource(TileObject.Source[] s) {
        super.setSubTileSource(s);
        cache.reset();
        handler.sendEmptyMessage(MapTile.MAPTILE_SUCCESS_ID);
    }


    @Override
    public void reDownloadTiles() {
        cache.reDownloadTiles(scontext);
    }
}  

