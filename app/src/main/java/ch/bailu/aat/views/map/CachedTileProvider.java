package ch.bailu.aat.views.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.services.cache.TileStackObject;

public class CachedTileProvider extends AbsOsmTileProvider {
    private Handler handler = new Handler();
    private final TileCache cache = new TileCache(10);

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

        AppBroadcaster.register(context, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
    }



    @Override
    public Drawable getMapTile(MapTile tile) {
        TileStackObject handle = cache.get(tile);



        if (handle == null) {
            handle = getTileHandle(tile);

            cache.put(handle);
        } 
        return handle.getDrawable(context.getResources());
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
        handler.sendEmptyMessage(MapTile.MAPTILE_SUCCESS_ID);
    }


    @Override
    public void reDownloadTiles() {
        cache.reDownloadTiles(scontext);
    }
}  

