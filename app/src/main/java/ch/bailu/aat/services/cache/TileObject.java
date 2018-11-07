package ch.bailu.aat.services.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.graphic.SyncTileBitmap;

public abstract class TileObject extends ObjectHandle {

    private final int hash;

    public TileObject(String id) {
        super(id);
        hash = id.hashCode();
    }


    public abstract Drawable getDrawable(Resources r);
    public abstract Bitmap getAndroidBitmap();
    public abstract TileBitmap getTileBitmap();
    public abstract Tile getTile();
    public abstract void reDownload(ServiceContext sc);

    public abstract boolean isLoaded();



    protected static long getSize(SyncTileBitmap bitmap, long defaultSize) {
        long size = bitmap.getSize();

        if (size == 0) size = defaultSize;

        return size;
    }


    @Override
    public int hashCode() {
        return hash;
    }


    public static final TileObject NULL_TILE = new TileObject("") {
        @Override
        public Drawable getDrawable(Resources r) {
            return null;
        }

        @Override
        public Bitmap getAndroidBitmap() {
            return null;
        }

        @Override
        public TileBitmap getTileBitmap() {
            return null;
        }

        @Override
        public Tile getTile() {
            return null;
        }

        @Override
        public void reDownload(ServiceContext sc) {

        }

        @Override
        public boolean isLoaded() {
            return false;
        }

        @Override
        public long getSize() {
            return MIN_SIZE;
        }

        @Override
        public void onDownloaded(String id, String url, ServiceContext sc) {

        }

        @Override
        public void onChanged(String id, ServiceContext sc) {

        }
    };
}
