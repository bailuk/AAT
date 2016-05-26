package ch.bailu.aat.views.map;

import org.osmdroid.tileprovider.MapTile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.services.cache.TileStackObject;

public class CachedTileProvider extends AbsOsmTileProvider {
    private Handler handler = new Handler();
    private final TileCache cache = new TileCache(10);

    private final Context context;


    private BroadcastReceiver onFileChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String string = AppBroadcaster.getFile(intent);
            if (cache.get((string)) != null) {
                handler.sendEmptyMessage(MapTile.MAPTILE_SUCCESS_ID);
            }
        }

    };




    public CachedTileProvider(ServiceContext sc)  {
        super(sc);
        context = sc.getContext();

        AppBroadcaster.register(context, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
    }



    @Override
    public Drawable getMapTile(MapTile tile) {
        TileStackObject handle = cache.get(tile);



        if (handle == null) {
            handle = getTileHandle(tile);

            cache.put(handle);
        } 
        return handle.getDrawable();
    }



    @Override
    public void detach() {

        context.unregisterReceiver(onFileChanged);
        cache.close();
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
    }


    @Override
    public void deleteVisibleTilesFromDisk() {
        cache.deleteFromDisk();
        handler.sendEmptyMessage(MapTile.MAPTILE_SUCCESS_ID);
    }
}  

