package ch.bailu.aat.services.cache;

import android.content.Context;
import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import java.io.File;

import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.DownloadHandle;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.simpleio.foc.Foc;


public class BitmapTileObject extends TileObject {
    private final DownloadSource source;
    private final Tile tile;


    private final String url;


    private final SyncTileBitmap bitmap=new SyncTileBitmap();


    private final DownloadHandle download;


    public BitmapTileObject(String id, final ServiceContext sc,  Tile t, DownloadSource s) {
        super(id);
        tile = t;
        source=s;
        url = source.getTileURLString(tile);
        download = new FileDownloader(url, FocAndroid.factory(sc.getContext(),toString()), sc);

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
        if (isLoadable()) sc.getBackgroundService().load(new FileLoader(toString(), sc));
        else if (isDownloadable())
            sc.getBackgroundService().download(download);
    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }


    @Override
    public void reDownload(ServiceContext sc) {
        if (download.isLocked()==false) {
            toFile(sc.getContext()).rm();
            if (isDownloadable()) sc.getBackgroundService().download(download);
        }
    }


    @Override
    public boolean isLoaded() {
        return getBitmap() != null;
    }


    private boolean isLoadable() {
        File file =  new File(toString());

        return file.isFile() && file.canRead();
    }

    private boolean isDownloadable() {
        return (
                !new File(toString()).exists() &&
                        source.getMaximumZoomLevel() >= tile.zoomLevel &&
                        source.getMinimumZoomLevel() <= tile.zoomLevel);
    }


    @Override
    public void onDownloaded(String id, String u, ServiceContext sc) {
        if (u.equals(url) && isLoadable()) {
            sc.getBackgroundService().load(new FileLoader(toString(), sc));
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
        private final ServiceContext scontext;

        public FileLoader(String f, ServiceContext sc) {
            super(f);
            scontext = sc;
        }

        @Override
        public long bgOnProcess() {
            long size = 0;
            if (scontext.lock()) {
                File file = new File(toString());

                ObjectHandle obj = scontext.getCacheService().getObject(toString());

                if (obj instanceof BitmapTileObject) {
                    BitmapTileObject bmp = (BitmapTileObject) obj;
                    bmp.bitmap.set(file, TILE_SIZE, bmp.source.isTransparent());
                    size = bmp.getSize();

                }
                obj.free();
                scontext.free();
            }
            return size;
        }


        @Override
        public void broadcast(Context context) {
            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, toString());
        }
    }

    private static class FileDownloader extends DownloadHandle {

        private final ServiceContext scontext;

        public FileDownloader(String source, Foc target, ServiceContext sc) {
            super(source, target);
            scontext = sc;
        }



        @Override
        public long bgOnProcess() {
            if (isInCache()) {
                    return super.bgOnProcess();
            }
            return 0;
        }

        private boolean isInCache() {
            boolean inCache = false;
            if (scontext.lock()) {
                ObjectHandle obj = scontext.getCacheService().getObject(getFile().toString());
                inCache = obj instanceof BitmapTileObject;
                obj.free();
                scontext.free();
            }
            return inCache;
        }
    }
}
