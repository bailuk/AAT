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
import ch.bailu.aat.util.graphic.SyncTileBitmap;


public class BitmapTileObject extends TileObject {
    private final DownloadSource source;
    private final Tile tile;


    private final FileHandle load;
    private final DownloadHandle download;
    private final String url;


    private final SyncTileBitmap bitmap=new SyncTileBitmap();


    public BitmapTileObject(String id, ServiceContext cs,  Tile t, DownloadSource s) {
        super(id);
        tile = t;
        source=s;
        url = source.getTileURLString(tile);

        cs.getCacheService().addToBroadcaster(this);
        download = new DownloadHandle(url, new File(id) );
        
        load = new FileHandle(id) {

            @Override
            public long bgOnProcess() {
                if (download.isLocked()==false) {

                    File file = new File(toString());
                    bitmap.set(file, TILE_SIZE, source.isTransparent());

                }
                return bitmap.getSize();
            }

            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, BitmapTileObject.this.toString());
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
        else if (isDownloadable()) sc.getBackgroundService().download(download);
    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }

    @Override
    public void reDownload(ServiceContext sc) {
        if (download.isLocked()==false) {
            toFile().delete();
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
            sc.getBackgroundService().load(load);
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
}
