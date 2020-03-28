package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.map.tile.TileFlags;
import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.preferences.map.SolidTileSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.DownloadTask;
import ch.bailu.aat.services.background.FileTask;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.util_java.foc.Foc;


public final class BitmapTileObject extends TileObject {

    private final Foc file;
    private final Tile tile;
    private final SyncTileBitmap bitmap=new SyncTileBitmap();

    private final String url;             //**
    private final DownloadSource source;  //**



    public BitmapTileObject(String id, final ServiceContext sc,  Tile t, DownloadSource s) {
        super(id);
        tile = t;
        file = FocAndroid.factory(sc.getContext(), id);

        source=s;                               //**
        url = source.getTileURLString(tile);    //**

        sc.getCacheService().addToBroadcaster(this);
    }

    @Override
    public TileBitmap getTileBitmap() {return bitmap.getTileBitmap();}

    @Override
    public Tile getTile() {return tile;}

    @Override
    public void onInsert(ServiceContext sc) {
        if (isLoadable()) sc.getBackgroundService().process(new TileLoaderTask(file));
        else if (isDownloadable() && sc.getBackgroundService().findTask(file) == null) //**
            sc.getBackgroundService().process(new FileDownloader(url, file, sc));      //**
    }




    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }


    @Override
    public void reDownload(ServiceContext sc) {
        if (sc.getBackgroundService().findTask(file) == null) {                        //**
            file.rm();                                                                 //**
            if (isDownloadable())                                                      //**
                sc.getBackgroundService().process(new FileDownloader(url, file, sc));  //**
        }
    }


    @Override
    public boolean isLoaded() {return getAndroidBitmap() != null;}

    private boolean isLoadable() {
        file.update();
        return file.isFile() && file.canRead();
    }



    public boolean isReadyAndLoaded() {
        boolean loaded = isLoaded();
        boolean notLoadable = isLoadable()==false;

        return loaded || notLoadable;
    }


    @Override
    public void onChanged(String id, ServiceContext sc) {}

    private boolean isDownloadable() {    //**
        return (
                !file.exists() &&
                        source.getMaximumZoomLevel() >= tile.zoomLevel &&
                        source.getMinimumZoomLevel() <= tile.zoomLevel);
    }


    @Override
    public void onDownloaded(String id, String u, ServiceContext sc) {
        if (u.equals(url) && isLoadable()) {                                    //**
            sc.getBackgroundService().process(new TileLoaderTask(file));        //**
        }

    }

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

            new OnObject(sc, getFile().getPath(), BitmapTileObject.class) {
                @Override
                public void run(ObjectHandle handle) {
                    BitmapTileObject tile = (BitmapTileObject) handle;

                    tile.bitmap.set(
                            getFile(),
                            SolidTileSize.DEFAULT_TILESIZE,
                            TileFlags.ALWAYS_TRANSPARENT || tile.source.isTransparent()
                    );

                    size[0] = tile.getSize();

                    AppBroadcaster.broadcast(sc.getContext(),
                            AppBroadcaster.FILE_CHANGED_INCACHE,
                            getFile().getPath());
                }
            };
            return size[0];
        }


    }

    private static class FileDownloader extends DownloadTask {

        private final ServiceContext scontext;

        public FileDownloader(String source, Foc target, ServiceContext sc)  {
            super(sc.getContext(), source, target);
            scontext = sc;
        }


        @Override
        public long bgOnProcess(ServiceContext sc) {
            if (isInCache()) {
                return super.bgOnProcess(sc);
            }
            return 0;
        }

        private boolean isInCache() {
            final boolean[] inCache = {false};

            new OnObject(scontext, getFile().getPath(), BitmapTileObject.class) {
                @Override
                public void run(ObjectHandle handle) {
                    inCache[0] = true;
                }
            };
            return inCache[0];
        }
    }


    public static class Factory extends ObjectHandle.Factory {
        private final Tile tile;
        private final DownloadSource source;


        public Factory(Tile t, DownloadSource s) {
            tile = t;
            source = s;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new BitmapTileObject(id, cs, tile, source);
        }
    }
}
