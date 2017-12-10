package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import java.net.MalformedURLException;

import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.DownloadHandle;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.util_java.foc.Foc;


public class BitmapTileObject extends TileObject {
    private final DownloadSource source;
    private final Tile tile;

    private final String url;
    private final Foc file;

    private final SyncTileBitmap bitmap=new SyncTileBitmap();
    private final DownloadHandle download;



    public BitmapTileObject(String id, final ServiceContext sc,  Tile t, DownloadSource s) {
        super(id);
        tile = t;
        source=s;

        file = FocAndroid.factory(sc.getContext(), id);

        url = source.getTileURLString(tile);
        download = new FileDownloader(url, file, sc);

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
        if (isLoadable()) sc.getBackgroundService().process(new FileLoader(file));
        else if (isDownloadable())
            sc.getBackgroundService().process(download);
    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }


    @Override
    public void reDownload(ServiceContext sc) {
        if (download.isLocked()==false) {
            file.rm();
            if (isDownloadable()) sc.getBackgroundService().process(download);
        }
    }


    @Override
    public boolean isLoaded() {
        return getBitmap() != null;
    }




    private boolean isLoadable() {
        file.update();
        return file.isFile() && file.canRead();
    }

    private boolean isDownloadable() {
        return (
                !file.exists() &&
                        source.getMaximumZoomLevel() >= tile.zoomLevel &&
                        source.getMinimumZoomLevel() <= tile.zoomLevel);
    }


    @Override
    public void onDownloaded(String id, String u, ServiceContext sc) {
        if (u.equals(url) && isLoadable()) {
            sc.getBackgroundService().process(new FileLoader(file));
        }

    }


    @Override
    public void onChanged(String id, ServiceContext sc) {}



    public boolean isReadyAndLoaded() {
        boolean loaded = isLoaded();
        boolean notLoadable = isLoadable()==false;

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

    @Override
    public Foc getFile() {
        return file;
    }


    public static class Factory extends ObjectHandle.Factory {
        private final Tile mapTile;
        private final DownloadSource source;


        public Factory(Tile mt, DownloadSource s) {
            mapTile=mt;
            source = s;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new BitmapTileObject(id, cs, mapTile, source);
        }
    }



    private static class FileLoader extends FileHandle {

        public FileLoader(Foc f) {
            super(f);
        }

        @Override
        public long bgOnProcess(final ServiceContext scontext) {
            final long[] size = {0};

            new OnObject(scontext, getFile().getPath(), BitmapTileObject.class) {
                @Override
                public void run(ObjectHandle handle) {
                    BitmapTileObject bmp = (BitmapTileObject) handle;
                    bmp.bitmap.set(bmp.getFile(), TILE_SIZE, bmp.source.isTransparent());
                    size[0] = bmp.getSize();

                    AppBroadcaster.broadcast(scontext.getContext(),
                            AppBroadcaster.FILE_CHANGED_INCACHE,
                            getFile().getPath());
                }
            };
            return size[0];
        }


    }

    private static class FileDownloader extends DownloadHandle {

        private final ServiceContext scontext;

        public FileDownloader(String source, Foc target, ServiceContext sc)  {
            super(source, target);
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
}
