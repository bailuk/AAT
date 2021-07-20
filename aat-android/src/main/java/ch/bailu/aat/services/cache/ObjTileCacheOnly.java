package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.map.tile.TileFlags;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.map.SolidTileSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileTask;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;
import ch.bailu.aat_lib.util.Objects;

public class ObjTileCacheOnly extends ObjTile {

    private final Foc file;
    private final Tile tile;
    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private final Source source;



    public ObjTileCacheOnly(String id, ServiceContext sc, Tile t, Source s) {
        super(id);
        tile = t;
        file = FocAndroid.factory(sc.getContext(), id);

        source = s;

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
        if (isLoadable()) load(sc);
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


    protected boolean isLoadable() {
        file.update();
        return file.isFile() && file.canRead();
    }


    @Override
    public void onDownloaded(String id, String u, ServiceContext sc) {
        if (Objects.equals(id, getID()) && isLoadable()) {
            load(sc);
        }
    }


    protected boolean fileExists() {
        file.update();
        return getFile().exists();
    }

    protected void load(ServiceContext sc) {
        sc.getBackgroundService().process(new TileLoaderTask(file));
    }


    public boolean isReadyAndLoaded() {
        boolean loaded = isLoaded();
        boolean notLoadable = isLoadable() == false;

        return loaded || notLoadable;
    }


    @Override
    public void onChanged(String id, ServiceContext sc) {}



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


            new OnObject(sc, getFile().getPath(), ObjTileCacheOnly.class) {
                @Override
                public void run(Obj handle) {
                    ObjTileCacheOnly tile = (ObjTileCacheOnly) handle;

                    tile.bitmap.set(
                            getFile(),
                            SolidTileSize.DEFAULT_TILESIZE,
                            TileFlags.ALWAYS_TRANSPARENT || tile.source.isTransparent());


                    OldAppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE,
                            getFile());

                    size[0] =  tile.bitmap.getSize();


                }
            };
            return size[0];


        }
    }


    public static class Factory extends Obj.Factory {
        private final Tile tile;
        private final Source source;


        public Factory(Tile t, Source s) {
            tile = t;
            source = s;
        }

        @Override
        public Obj factory(String id, ServiceContext cs) {
            return new ObjTileCacheOnly(id, cs, tile, source);
        }
    }
}

