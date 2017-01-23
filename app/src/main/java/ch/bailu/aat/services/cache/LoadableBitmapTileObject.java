package ch.bailu.aat.services.cache;

import android.content.Context;
import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import java.io.File;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;

public class LoadableBitmapTileObject extends TileObject {

    private final Tile tile;
    private final FileHandle load;


    private final SyncTileBitmap bitmap = new SyncTileBitmap();


    public LoadableBitmapTileObject(String id, ServiceContext cs, Tile t, final boolean transparent) {
        super(id);
        tile = t;

        cs.getCacheService().addToBroadcaster(this);

        load = new FileHandle(id) {

            @Override
            public long bgOnProcess() {

                File file = new File(toString());
                bitmap.set(file, TILE_SIZE, transparent);

                return bitmap.getSize();
            }

            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE,
                        LoadableBitmapTileObject.this.toString());
            }
        };

    }

    @Override
    public TileBitmap getTileBitmap() {
        return bitmap.getTileBitmap();
    }

    @Override
    public Tile getTile() {
        return tile;
    }

    @Override
    public void onInsert(ServiceContext sc) {
        if (isLoadable()) sc.getBackgroundService().load(load);
    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }

    @Override
    public void reDownload(ServiceContext sc) {
    }

    @Override
    public boolean isLoaded() {
        return getBitmap() != null;
    }


    private boolean isLoadable() {
        File file = new File(toString());

        return file.isFile() && file.canRead();
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}

    @Override
    public void onChanged(String id, ServiceContext sc) {
    }


    @Override
    public boolean isReady() {
        boolean loaded = isLoaded();
        boolean notLoadable = isLoadable() == false;

        return loaded || notLoadable;
    }


    @Override
    public long getSize() {
        return getBytesHack(TILE_SIZE);
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap.getAndroidBitmap();
    }


    public static class Factory extends ObjectHandle.Factory {
        private final Tile mapTile;
        private final boolean isTransparent;


        public Factory(Tile mt, Boolean t) {
            mapTile = mt;
            isTransparent = t;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new LoadableBitmapTileObject(id, cs, mapTile, isTransparent);
        }
    }
}

