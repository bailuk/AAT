package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.graphic.AppTileBitmap;
import ch.bailu.aat.util.graphic.SynchronizedBitmap;


public class EmptyTileObject extends TileObject {
    public static final SynchronizedBitmap NULL_BITMAP=createBitmap();
    
    public EmptyTileObject(String id) {
        super(id);
        
    }

    @Override
    public long getSize() {
        return MIN_SIZE;
    }

    private static SynchronizedBitmap createBitmap() {
        SynchronizedBitmap bitmap = new SynchronizedBitmap();
        bitmap.set(new AppTileBitmap(TILE_SIZE));

        return bitmap;
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}

    @Override
    public void onChanged(String id, ServiceContext sc) {}

    @Override
    public Bitmap getBitmap() {
        return NULL_BITMAP.getAndroidBitmap();
    }

    @Override
    public void reDownload(ServiceContext sc) {

    }

    @Override
    public boolean isLoaded() {
        return true;
    }

}
