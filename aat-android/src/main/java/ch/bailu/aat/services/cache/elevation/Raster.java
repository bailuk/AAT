package ch.bailu.aat.services.cache.elevation;

import android.graphics.Point;
import android.graphics.Rect;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;

import java.util.ArrayList;

import ch.bailu.aat.preferences.map.SolidTileSize;
import ch.bailu.aat.services.cache.Span;
import ch.bailu.aat.services.elevation.tile.DemGeoToIndex;

public final class Raster {
    public final int[] toLaRaster = new int[SolidTileSize.DEFAULT_TILESIZE];
    public final int[] toLoRaster = new int[SolidTileSize.DEFAULT_TILESIZE];

    private boolean initialized=false;


    public synchronized void initialize(Tile tile, DemGeoToIndex geoToIndex, SubTiles tiles) {
            if (!initialized) {
                final ArrayList<Span> laSpan = new ArrayList<>(5);
                final ArrayList<Span> loSpan = new ArrayList<>(5);

                initializeWGS84Raster(laSpan, loSpan, tile);
                initializeIndexRaster(geoToIndex);
                tiles.generateSubTileList(laSpan, loSpan);

                initialized = true;
            }
    }


    public boolean isInitialized() {
        return initialized;
    }


    // 1. pixel to latitude
    private void initializeWGS84Raster(ArrayList<Span> laSpan, ArrayList<Span> loSpan, Tile tile) {
        final Rect tileR = getTileR(tile);

        final LatLong tl=pixelToGeo(tile.zoomLevel,
                tileR.left,
                tileR.top);

        final LatLong br=pixelToGeo(tile.zoomLevel,
                tileR.right,
                tileR.bottom);

        final int laDiff=br.getLatitudeE6()-tl.getLatitudeE6();
        final int loDiff=br.getLongitudeE6()-tl.getLongitudeE6(); //-1.2 - -1.5 = 0.3  //1.5 - 1.2 = 0.3


        int la = tl.getLatitudeE6();
        int lo = tl.getLongitudeE6();

        final Span laS=new Span((int) Math.floor(la/1e6d));
        final Span loS=new Span((int) Math.floor(lo/1e6d));

        final int laInc=laDiff / SolidTileSize.DEFAULT_TILESIZE;
        final int loInc=loDiff / SolidTileSize.DEFAULT_TILESIZE;

        int i;
        for (i=0; i< SolidTileSize.DEFAULT_TILESIZE; i++) {
            toLaRaster[i]=la;
            toLoRaster[i]=lo;

            final int laDeg = (int) Math.floor(la/1e6d);
            final int loDeg = (int) Math.floor(lo/1e6d);

            laS.copyIntoArray(laSpan, i, laDeg);
            loS.copyIntoArray(loSpan, i, loDeg);

            la+=laInc;
            lo+=loInc;
        }


        laS.copyIntoArray(laSpan,i);
        loS.copyIntoArray(loSpan,i);
    }


    // 2. pixel to dem index
    private void initializeIndexRaster(DemGeoToIndex toIndex) {
        for (int i=0; i< SolidTileSize.DEFAULT_TILESIZE; i++) {
            toLaRaster[i]=toIndex.toYPos(toLaRaster[i]);
            toLoRaster[i]=toIndex.toXPos(toLoRaster[i]);
        }
    }

    private LatLong pixelToGeo(byte z, int x, int y) {
        final long mapSize = MercatorProjection.getMapSize(z, SolidTileSize.DEFAULT_TILESIZE);
        return MercatorProjection.fromPixels(x, y, mapSize);
    }


    private Rect getTileR(Tile tile) {
        final Point tileTL = tileToPixel(tile);

        final Point tileBR = new Point(
                tileTL.x+SolidTileSize.DEFAULT_TILESIZE,
                tileTL.y+SolidTileSize.DEFAULT_TILESIZE);



        return new Rect(tileTL.x, tileTL.y, tileBR.x, tileBR.y);
    }

    private Point tileToPixel(Tile tile) {
        return new Point(tile.tileX*SolidTileSize.DEFAULT_TILESIZE,
                tile.tileY*SolidTileSize.DEFAULT_TILESIZE);
    }
}
