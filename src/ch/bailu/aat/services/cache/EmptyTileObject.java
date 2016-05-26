package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;
import ch.bailu.aat.services.ServiceContext;

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
    public void onDownloaded(String id, String url, ServiceContext sc) {}

    @Override
    public void onChanged(String id, ServiceContext sc) {}

    @Override
    public Bitmap getBitmap() {
        return NULL_BITMAP.get();
    }

}
