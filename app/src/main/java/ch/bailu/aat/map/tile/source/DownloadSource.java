package ch.bailu.aat.map.tile.source;

import android.content.Context;
import android.graphics.Paint;

import org.mapsforge.core.model.Tile;

import java.util.Random;

import ch.bailu.aat.services.cache.BitmapTileObject;
import ch.bailu.aat.util.fs.AppDirectory;

public class DownloadSource extends Source {
    public static final int MIN_ZOOM = 1;
    public static final int MAX_ZOOM =17; // 18 takes way too much space for the gain.

    public final static String EXT = ".png";

    private final Random random = new Random();
    private final int minZoom, maxZoom;

    private final String name;

    private final String[] urls;

    private final int alpha;
    private final boolean transparent;


    public DownloadSource(String n, int a, final String... url) {
        this(n, MIN_ZOOM, MAX_ZOOM, a, (a != OPAQUE), url);
    }

    public DownloadSource(String n, int minZ, int maxZ, int a, final String... url) {
        this(n, minZ, maxZ, a, (a != OPAQUE), url);
    }


    public DownloadSource(String n, int minZ, int maxZ, int a, boolean t, String... u) {
        name = n;
        minZoom = minZ;
        maxZoom = maxZ;
        urls = u;
        alpha = a;
        transparent = (a != OPAQUE);
    }


    public String getName() {
        return name;
    }


    @Override
    public String getID(Tile tile, Context context) {
        return AppDirectory.getTileFile(getTileRelativeFilename(tile), context).getPath();
    }

    @Override
    public int getPaintFlags() {
        return 0;
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
    public BitmapTileObject.Factory getFactory(Tile mt) {
        return new BitmapTileObject.Factory(mt, this);
    }

    public String getTileURLString(Tile tile) {
        return getBaseUrl() + tile.zoomLevel + "/" + tile.tileX + "/" + tile.tileY + EXT;
    }

    private String getBaseUrl() {
        return urls[random.nextInt(urls.length)];
    }



    private String getTileRelativeFilename(final Tile tile) {
        return  name +
                '/' +
                tile.zoomLevel +
                '/' +
                tile.tileX +
                '/' +
                tile.tileY +
                EXT;

    }


    public final static DownloadSource MAPNIK =
            new DownloadSource("Mapnik",
                    OPAQUE,
                    "http://a.tile.openstreetmap.org/",
                    "http://b.tile.openstreetmap.org/",
                    "http://c.tile.openstreetmap.org/") {
                @Override
                public int getPaintFlags() {
                    return Paint.FILTER_BITMAP_FLAG;
                }
            };


    public final static DownloadSource TRAIL_MTB =
            new DownloadSource("TrailMTB",
                    TRANSPARENT,
                    "http://tile.waymarkedtrails.org/mtb/");

    public final static DownloadSource TRAIL_SKATING =
            new DownloadSource("TrailSkating",
                    TRANSPARENT,
                    "http://tile.waymarkedtrails.org/skating/");


    public final static DownloadSource TRAIL_HIKING =
            new DownloadSource("TrailHiking",
                    TRANSPARENT,
                    "http://tile.waymarkedtrails.org/hiking/");


    public final static DownloadSource TRAIL_CYCLING =
            new DownloadSource("TrailCycling",
                    TRANSPARENT,
                    "http://tile.waymarkedtrails.org/cycling/");



    public final static DownloadSource TRANSPORT_OVERLAY =
            new DownloadSource("OpenPtMap",
                    5, 16,
                    TRANSPARENT,
                    "http://openptmap.org/tiles/");

}
