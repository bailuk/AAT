//package ch.bailu.aat.map.tile;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//
//import org.mapsforge.core.graphics.TileBitmap;
//import org.mapsforge.core.model.Tile;
//
//import ch.bailu.aat.util.AppBroadcaster;
//import ch.bailu.aat.util.AppIntent;
//import ch.bailu.aat.services.ServiceContext;
//import ch.bailu.aat.services.cache.TileObject;
//import ch.bailu.aat.services.cache.TileStackObject;
//
//public class TileProviderCached extends TileProviderAbstract {
//
//
//    private TileCache<TileStackObject> cache = TileCache.NULL;
//
//    private final Context context;
//    private final ServiceContext scontext;
//
//
//    private final BroadcastReceiver onFileChanged = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String string = AppIntent.getFile(intent);
//            if (cache.get((string)) != null) {
//                notifyChange();
//            }
//        }
//
//    };
//
//
//
//    public TileProviderCached(ServiceContext sc)  {
//        super(sc);
//        context = sc.getContext();
//        scontext = sc;
//
//    }
//
//
//
//    @Override
//    public Drawable getDrawable(Tile tile) {
//        return getMapTileStack(tile).getDrawable(context.getResources());
//    }
//
//    @Override
//    public TileBitmap get(Tile tile) {
//        return getMapTileStack(tile).getTileBitmap();
//    }
//
//
//    private TileStackObject getMapTileStack(Tile tile) {
//        TileStackObject handle = cache.get(tile);
//
//
//        if (handle == null) {
//            handle = getTileHandle(tile);
//
//            cache.put(handle);
//        }
//
//        return handle;
//    }
//
//
//
//    @Override
//    public void onAttached() {
//        AppBroadcaster.register(context, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
//        cache = new LockTileCache();
//    }
//
//
//    @Override
//    public void onDetached() {
//
//        context.unregisterReceiver(onFileChanged);
//        cache.close();
//        cache = TileCache.NULL;
//    }
//
//
//
//    @Override
//    public boolean contains(Tile tile) {
//        return cache.get(tile) != null;
//    }
//
//
//
//    @Override
//    public int getCapacity() {
//        return cache.pixelCount();
//    }
//
//    @Override
//    public void setCapacity(int capacity) {
//        cache.setCapacity(capacity);
//    }
//
//
//    @Override
//    public void setSubTileSource(TileObject.Source[] s) {
//        super.setSubTileSource(s);
//        cache.reset();
//    }
//
//
//    @Override
//    public void reDownloadTiles() {
//        cache.reDownloadTiles(scontext);
//    }
//}
//
