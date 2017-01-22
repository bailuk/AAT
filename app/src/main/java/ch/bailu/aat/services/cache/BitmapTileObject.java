package ch.bailu.aat.services.cache;

import android.content.Context;
import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import java.io.File;
import java.util.Random;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.DownloadHandle;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.graphic.SyncTileBitmap;


public class BitmapTileObject extends TileObject {
    private final Source source;
    private final Tile tile;


    private final FileHandle load;
    private final DownloadHandle download;
    private final String url;


    private final SyncTileBitmap bitmap=new SyncTileBitmap();


    public BitmapTileObject(String id, ServiceContext cs,  Tile t, Source s) {
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
    
    

    @Override
    public boolean isReady() {
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
        private final Source source;

        
        public Factory(Tile mt, Source s) {
            mapTile=mt;
            source = s;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return new BitmapTileObject(id, cs, mapTile, source);
        }
    }


    public static final int MIN_ZOOM = 1;
    public static final int MAX_ZOOM =17; // 18 takes way too much space for the gain.

    public static class Source extends TileObject.Source {
        public final static String EXT = ".png";

        private final Random random = new Random();
        private final int minZoom, maxZoom;

        private final String name;

        private final String[] urls;

        private final int alpha;
        private boolean transparent;


        public Source (String n, int a, final String... url) {
            this(n, MIN_ZOOM, MAX_ZOOM, a, (a != OPAQUE), url);
        }

        public Source (String n, int minZ, int maxZ, int a, final String... url) {
            this(n, minZ, maxZ, a, (a != OPAQUE), url);
        }

        public Source(String n, int minZ, int maxZ, int a, boolean t, String... u) {
            name = n;
            minZoom = minZ;
            maxZoom = maxZ;
            urls = u;
            alpha = a;
            transparent = (a != OPAQUE);
        }


        @Override
        public String getName() {
            return name;
        }
        
        
        @Override
        public String getID(Tile tile, Context context) {
            return AppDirectory.getTileFile(tile,
                    getTileRelativeFilename(tile), context).getAbsolutePath();
        }

        @Override
        public int getMinimumZoomLevel() {
            return minZoom;
        }

        @Override
        public int getMaximumZoomLevel() {
            return maxZoom;
        }

        public boolean isTransparent() {
            return transparent;
        }

        @Override
        public int getAlpha() {
            return alpha;
        }

        @Override
        public Factory getFactory(Tile mt) {
            return new BitmapTileObject.Factory(mt, this);
        }

        public String getTileURLString(Tile tile) {
            return getBaseUrl() + tile.zoomLevel + "/" + tile.tileX + "/" + tile.tileY + EXT;
        }

        private String getBaseUrl() {
            return urls[random.nextInt(urls.length)];
        }



        private String getTileRelativeFilename(final Tile tile) {
            final StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append('/');
            sb.append(tile.zoomLevel);
            sb.append('/');
            sb.append(tile.tileX);
            sb.append('/');
            sb.append(tile.tileY);
            sb.append(EXT);
            return sb.toString();
        }

    }




    public final static BitmapTileObject.Source MAPNIK =
            new BitmapTileObject.Source("Mapnik",
                    TileObject.Source.OPAQUE,
                    "http://a.tile.openstreetmap.org/",
                    "http://b.tile.openstreetmap.org/",
                    "http://c.tile.openstreetmap.org/");


    public final static TileObject.Source TRAIL_MTB =
            new BitmapTileObject.Source("TrailMTB",
                    TileObject.Source.TRANSPARENT,
                    "http://tile.waymarkedtrails.org/mtb/");

    public final static TileObject.Source TRAIL_SKATING =
            new BitmapTileObject.Source("TrailSkating",
                    TileObject.Source.TRANSPARENT,
                    "http://tile.waymarkedtrails.org/skating/");


    public final static TileObject.Source TRAIL_HIKING =
            new BitmapTileObject.Source("TrailHiking",
                    TileObject.Source.TRANSPARENT,
                    "http://tile.waymarkedtrails.org/hiking/");


    public final static TileObject.Source TRAIL_CYCLING =
            new BitmapTileObject.Source("TrailCycling",
                    TileObject.Source.TRANSPARENT,
                    "http://tile.waymarkedtrails.org/cycling/");



    public final static TileObject.Source TRANSPORT_OVERLAY =
            new BitmapTileObject.Source("OpenPtMap",
                    5, 16,
                    TileObject.Source.TRANSPARENT,
                    "http://openptmap.org/tiles/");


    public final static BitmapTileObject.Source HILLSHADE_CACHE =
            new BitmapTileObject.Source("HillShade",
                    NewHillshade.ELEVATION_HILLSHADE8.getMinimumZoomLevel(),
                    NewHillshade.ELEVATION_HILLSHADE8.getMaximumZoomLevel(),
                    NewHillshade.ELEVATION_HILLSHADE8.getAlpha(),
                    true,
                    "http://bailu.ch/");
}
