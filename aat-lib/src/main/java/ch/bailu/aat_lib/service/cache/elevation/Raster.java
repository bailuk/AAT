package ch.bailu.aat_lib.service.cache.elevation;


import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Tile;
import org.mapsforge.core.util.MercatorProjection;

import java.util.ArrayList;

import ch.bailu.aat_lib.preferences.map.SolidTileSize;
import ch.bailu.aat_lib.service.cache.Span;
import ch.bailu.aat_lib.service.elevation.tile.DemGeoToIndex;
import ch.bailu.aat_lib.util.Rect;

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

        final float laDiff = br.getLatitudeE6()  - tl.getLatitudeE6();
        final float loDiff = br.getLongitudeE6() - tl.getLongitudeE6(); //-1.2 - -1.5 = 0.3  //1.5 - 1.2 = 0.3


        float la = tl.getLatitudeE6();
        float lo = tl.getLongitudeE6();

        final Span laS = new Span();
        final Span loS = new Span();

        final float laInc = laDiff / (SolidTileSize.DEFAULT_TILESIZE);
        final float loInc = loDiff / (SolidTileSize.DEFAULT_TILESIZE);

        int i;
        for (i=0; i< SolidTileSize.DEFAULT_TILESIZE; i++) {
            toLaRaster[i]=Math.round(la);
            toLoRaster[i]=Math.round(lo);

            final int laDeg = (int) Math.floor(la/1e6f);
            final int loDeg = (int) Math.floor(lo/1e6f);

            laS.incrementAndCopyIntoArray(laSpan, i, laDeg);
            loS.incrementAndCopyIntoArray(loSpan, i, loDeg);

            la+=laInc;
            lo+=loInc;
        }

        // flush
        laS.copyIntoArray(laSpan);
        loS.copyIntoArray(loSpan);
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
        Rect r = new Rect();
        r.top    = tile.tileY * SolidTileSize.DEFAULT_TILESIZE;
        r.left   = tile.tileX * SolidTileSize.DEFAULT_TILESIZE;
        r.right  = r.left + SolidTileSize.DEFAULT_TILESIZE - 1;
        r.bottom = r.top  + SolidTileSize.DEFAULT_TILESIZE - 1;
        return r;
    }
}
