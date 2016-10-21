package ch.bailu.aat.services.cache;

import android.content.Context;
import android.graphics.Bitmap;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.XYTileSource;

import java.io.File;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.DownloadHandle;
import ch.bailu.aat.services.background.FileHandle;


public class BitmapTileObject extends TileObject {
    private final Source source;
    private final MapTile tile;


    private final FileHandle load;
    private final DownloadHandle download;
    private final String url;


    private final SynchronizedBitmap bitmap=new SynchronizedBitmap();


    public BitmapTileObject(String id, ServiceContext cs,  MapTile t, Source s) {
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
                    bitmap.load(toString(),EmptyTileObject.NULL_BITMAP.get());
                    return bitmap.getSize();
                }
                return 0;
            }

            @Override
            public void broadcast(Context context) {
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, BitmapTileObject.this.toString());
            }
        };
        
    }

    
    @Override
    public void onInsert(ServiceContext sc) {
        if (isLoadable()) sc.getBackgroundService().load(load);
        else if (isDownloadable()) sc.getBackgroundService().download(download);
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
        return bitmap.getDrawable()!=null;
    }


    private boolean isLoadable() {
        return new File(toString()).exists();
    }
    
    private boolean isDownloadable() {
        return (
                !new File(toString()).exists() &&
                source.getMaximumZoomLevel() >= tile.getZoomLevel() &&
                source.getMinimumZoomLevel() <= tile.getZoomLevel());
    }
    
    
    @Override
    public void onDownloaded(String id, String u, ServiceContext sc) {
        if (u.equals(url) && isLoadable()) {
            sc.getBackgroundService().load(load);
        }
        
    }


    @Override
    public void onChanged(String id, ServiceContext sc) {}
    
    

    @Override
    public boolean isReady() {
        boolean d = bitmap.getDrawable()!=null;
        boolean l = isLoadable()==false;
        
        return  d || l;
    }

    
    @Override
    public long getSize() {
        return bitmap.getSize();
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap.get();
    }


  




    public static class Factory extends ObjectHandle.Factory {
        private final MapTile mapTile;
        private final Source source;

        
        public Factory(MapTile mt, Source s) {
            mapTile=mt;
            source = s;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new BitmapTileObject(id, cs, mapTile, source);
        }
    }
    
    public static class Source extends TileObject.Source {

        private final XYTileSource osmdroidSource;
        private final TileBitmapFilter filter;

        
        
        public Source (String sourceName, TileBitmapFilter f, int minZ, int maxZ, final String... url) {
            osmdroidSource = new XYTileSource(sourceName, minZ, maxZ, ".png", url);
            filter = f;
        }
        

        @Override
        public String getName() {
            return osmdroidSource.name();
        }
        
        
        @Override
        public String getID(MapTile mt, Context context) {
            return AppDirectory.getTileFile(mt, osmdroidSource, context).getAbsolutePath();
        }

        @Override
        public int getMinimumZoomLevel() {
            return osmdroidSource.getMinimumZoomLevel();
        }

        @Override
        public int getMaximumZoomLevel() {
            return osmdroidSource.getMaximumZoomLevel();
        }

        @Override
        public Factory getFactory(MapTile mt) {
            return new BitmapTileObject.Factory(mt, this);
        }

        public String getTileURLString(MapTile tile) {
            return osmdroidSource.getTileURLString(tile);
        }


        @Override
        public TileBitmapFilter getBitmapFilter() {
            return filter;
        }
    }


    public static final int MIN_ZOOM = 1;
    public static final int MAX_ZOOM=17; // 18 takes way too much space for the gain.


    public final static BitmapTileObject.Source MAPNIK_GRAY =
            new BitmapTileObject.Source("Mapnik", TileBitmapFilter.GRAYSCALE_FILTER,
                    MIN_ZOOM, MAX_ZOOM,
                    "http://a.tile.openstreetmap.org/",
                    "http://b.tile.openstreetmap.org/",
                    "http://c.tile.openstreetmap.org/");


    public final static BitmapTileObject.Source MAPNIK =
            new BitmapTileObject.Source("Mapnik", TileBitmapFilter.OVERLAY_FILTER,
                    MIN_ZOOM, MAX_ZOOM,
                    "http://a.tile.openstreetmap.org/",
                    "http://b.tile.openstreetmap.org/",
                    "http://c.tile.openstreetmap.org/");


    public final static TileObject.Source TRAIL_MTB =
            new BitmapTileObject.Source("TrailMTB",  TileBitmapFilter.OVERLAY_FILTER, MIN_ZOOM, MAX_ZOOM,
                    "http://tile.waymarkedtrails.org/mtb/");

    public final static TileObject.Source TRAIL_SKATING =
            new BitmapTileObject.Source("TrailSkating",TileBitmapFilter.OVERLAY_FILTER, MIN_ZOOM, MAX_ZOOM,
                    "http://tile.waymarkedtrails.org/skating/");


    public final static TileObject.Source TRAIL_HIKING =
            new BitmapTileObject.Source("TrailHiking", TileBitmapFilter.OVERLAY_FILTER, MIN_ZOOM, MAX_ZOOM,
                    "http://tile.waymarkedtrails.org/hiking/");


    public final static TileObject.Source TRAIL_CYCLING =
            new BitmapTileObject.Source("TrailCycling", TileBitmapFilter.OVERLAY_FILTER, MIN_ZOOM, MAX_ZOOM,
                    "http://tile.waymarkedtrails.org/cycling/");



    public final static TileObject.Source TRANSPORT_OVERLAY =
            new BitmapTileObject.Source("OpenPtMap", TileBitmapFilter.OVERLAY_FILTER, 5, 16,
                    "http://openptmap.org/tiles/");


    public final static BitmapTileObject.Source HILLSHADE_CACHE =
            new BitmapTileObject.Source("HillShade", TileBitmapFilter.COPY_FILTER,
                    NewHillshade.ELEVATION_HILLSHADE8.getMinimumZoomLevel(),
                    NewHillshade.ELEVATION_HILLSHADE8.getMaximumZoomLevel(),
                    "http://bailu.ch/");

}
