package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.map.tile.TileFlags;
import ch.bailu.aat.preferences.map.SolidTileSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileTask;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.util_java.foc.Foc;

public final class CacheOnlyTileObject extends TileObject {

    private final Foc file;
    private final Tile tile;
    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private final boolean isTransparent;



    public CacheOnlyTileObject(String id, ServiceContext sc, Tile t, final boolean transparent) {
        super(id);
        tile = t;
        file = FocAndroid.factory(sc.getContext(), id);

        isTransparent = transparent;

        sc.getCacheService().addToBroadcaster(this);

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
        if (isLoadable()) sc.getBackgroundService().process(new TileLoaderTask(file));
    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }

    @Override
    public void reDownload(ServiceContext sc) {}

    @Override
    public boolean isLoaded() {
        return getAndroidBitmap() != null;
    }


    private boolean isLoadable() {
        // **? file.update();
        return file.isFile() && file.canRead();
    }


    public boolean isReadyAndLoaded() {
        boolean loaded = isLoaded();
        boolean notLoadable = isLoadable() == false;

        return loaded || notLoadable;
    }


    @Override
    public void onChanged(String id, ServiceContext sc) {}

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}



    @Override
    public long getSize() {
        return getSize(bitmap, SolidTileSize.DEFAULT_TILESIZE_BYTES);
    }



    @Override
    public Bitmap getAndroidBitmap() {
        return bitmap.getAndroidBitmap();
    }

    @Override
    public Foc getFile() {
        return file;
    }

    private static class TileLoaderTask extends FileTask {

        public TileLoaderTask(Foc f) {
            super(f);
        }

        @Override
        public long bgOnProcess(final ServiceContext sc) {
            final long[] size = {0};


            new OnObject(sc, getFile().getPath(), CacheOnlyTileObject.class) {
                @Override
                public void run(ObjectHandle handle) {
                    CacheOnlyTileObject tile = (CacheOnlyTileObject) handle;

                    tile.bitmap.set(
                            getFile(),
                            SolidTileSize.DEFAULT_TILESIZE,
                            TileFlags.ALWAYS_TRANSPARENT || tile.isTransparent);


                    AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE,
                            getFile());

                    size[0] =  tile.bitmap.getSize();


                }
            };
            return size[0];


        }
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
            return new CacheOnlyTileObject(id, cs, mapTile, isTransparent);
        }
    }
}

