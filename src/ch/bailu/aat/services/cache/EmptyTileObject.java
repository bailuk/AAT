package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;
import ch.bailu.aat.services.cache.CacheService.SelfOn;

public class EmptyTileObject extends TileObject {
    public static final SynchronizedBitmap NULL_BITMAP=createBitmap();
    
    public EmptyTileObject(String id) {
        super(id);
        
    }

    private static SynchronizedBitmap createBitmap() {
        SynchronizedBitmap bitmap = new SynchronizedBitmap();
        bitmap.set(SynchronizedBitmap.createBitmap(TILE_SIZE, TILE_SIZE));
        return bitmap;
    }

    @Override
    public void onDownloaded(String id, String url, SelfOn self) {}

    @Override
    public void onChanged(String id, SelfOn self) {}

    @Override
    public Bitmap getBitmap() {
        return NULL_BITMAP.get();
    }

}
