package ch.bailu.aat.services.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.DownloadTask;
import ch.bailu.aat.services.background.FileTask;
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



    public BitmapTileObject(String id, final ServiceContext sc,  Tile t, DownloadSource s) {
        super(id);
        tile = t;
        source=s;

        file = FocAndroid.factory(sc.getContext(), id);

        url = source.getTileURLString(tile);

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
        if (isLoadable()) sc.getBackgroundService().process(new FileLoaderTask(file));
        else if (isDownloadable() && sc.getBackgroundService().findDownloadTask(file) == null)
            sc.getBackgroundService().process(new FileDownloader(url, file, sc));
    }




    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }


    @Override
    public void reDownload(ServiceContext sc) {
        if (sc.getBackgroundService().findDownloadTask(file) == null) {
            file.rm();
            if (isDownloadable()) sc.getBackgroundService().process(new FileDownloader(url, file, sc));
        }
    }


    @Override
    public boolean isLoaded() {
        return getAndroidBitmap() != null;
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
            sc.getBackgroundService().process(new FileLoaderTask(file));
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



    private static class FileLoaderTask extends FileTask {

        public FileLoaderTask(Foc f) {
            super(f);
        }

        @Override
        public long bgOnProcess(final ServiceContext scontext) {
            final long[] size = {0};

            new OnObject(scontext, getFile().getPath(), BitmapTileObject.class) {
                @Override
                public void run(ObjectHandle handle) {
                    BitmapTileObject bmp = (BitmapTileObject) handle;
                    bmp.bitmap.set(bmp.getFile(), SolidTileSize.DEFAULT_TILESIZE, bmp.source.isTransparent());
                    size[0] = bmp.getSize();

                    AppBroadcaster.broadcast(scontext.getContext(),
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
}
