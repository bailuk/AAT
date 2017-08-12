package ch.bailu.aat.services.cache.elevation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.Span;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.services.dem.tile.DemDimension;
import ch.bailu.aat.services.dem.tile.DemGeoToIndex;
import ch.bailu.aat.services.dem.tile.DemProvider;
import ch.bailu.aat.services.dem.tile.DemSplitter;
import ch.bailu.aat.services.dem.updater.ElevationUpdaterClient;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;

public abstract class ElevationTile extends TileObject implements ElevationUpdaterClient{

    private final Tile mapTile;
    private final int split;

    private boolean isPainting = false;

    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private final SubTiles subTiles = new SubTiles();
    private final Raster raster = new Raster();

    private static final int[] buffer = new int[TileObject.TILE_SIZE*TileObject.TILE_SIZE];


    public ElevationTile(String id, Tile _map_tile, int _split) {
        super(id);
        mapTile =_map_tile;
        split=_split;
    }


    @Override
    public TileBitmap getTileBitmap() {
        return bitmap.getTileBitmap();
    }

    @Override
    public Tile getTile() {
        return mapTile;
    }


    public DemProvider split(DemProvider dem) {
        int i=split;
        while(i>0) {
            dem=factorySplitter(dem);
            i--;
        }
        return dem;
    }

    public DemProvider factorySplitter(DemProvider dem) {
        return DemSplitter.factory(dem);
    }

    public DemGeoToIndex factoryGeoToIndex(DemDimension dim) {
        return new DemGeoToIndex(dim);
    }

    private DemGeoToIndex getGeoToIndex() {
        DemDimension dim=split(Dem3Tile.NULL).getDim();
        return factoryGeoToIndex(dim);
    }


    public abstract void fillBuffer(
            int[] bitmap,
            Raster raster,
            SubTile span,
            DemProvider demtile);


    @Override
    public void onInsert(ServiceContext sc) {
        sc.getCacheService().addToBroadcaster(this);

        if (!raster.isInizialized) {
            sc.getBackgroundService().process(new RasterInitializer(sc, getID()));
        }
    }


    public void onRemove(ServiceContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }


    @Override
    public void onChanged(String id, ServiceContext sc) {}



    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {
        if (subTiles.haveID(url) && raster.isInizialized) {
            AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.REQUEST_ELEVATION_UPDATE, toString());
        }
    }



    @Override
    public Bitmap getBitmap() {
        return bitmap.getAndroidBitmap();
    }


    public boolean isReadyAndLoaded() {
        return raster.isInizialized && isPainting == false;
    }


    @Override
    public boolean isLoaded() {
        return isReadyAndLoaded() && subTiles.size() == 0;
    }


    @Override
    public SrtmCoordinates[] getSrtmTileCoordinates() {
        return subTiles.toSrtmCoordinates();
    }



    @Override
    public void updateFromSrtmTile(ServiceContext cs, Dem3Tile tile) {
        final int key = tile.hashCode();
        final SubTile span =  subTiles.get(key);

        if (span != null && raster.isInizialized && tile.isLoaded()) {
            subTiles.remove(key);
            isPainting =true;

            cs.getBackgroundService().process(span.painterFactory(cs, getID(), tile));
        }
    }



    @Override
    public void reDownload(ServiceContext sc) {

    }

    public long bgOnProcessPainter(SubTile subTile, Dem3Tile dem3Tile) {

        final Rect interR = subTile.toRect();

        synchronized (raster) {
            if (raster.isInizialized) {
                synchronized (buffer) {
                    fillBuffer(buffer, raster, subTile, split(dem3Tile));


                    Bitmap t = bitmap.getAndroidBitmap();

                    if (t == null) {
                        bitmap.set(TILE_SIZE, true);
                        t = bitmap.getAndroidBitmap();
                        t.eraseColor(Color.TRANSPARENT);
                    }

                    t.setPixels(
                            buffer,
                            0,
                            interR.width(),
                            interR.left,
                            interR.top,
                            interR.width(),
                            interR.height());


                    isPainting = false;
                }
            }
        }
        return interR.width()*interR.height()*2;
    }



    public long bgOnProcessInitializer() {
        synchronized (raster) {
            if (!raster.isInizialized) {
                final ArrayList<Span> laSpan = new ArrayList<>(5);
                final ArrayList<Span> loSpan = new ArrayList<>(5);

                raster.initializeWGS84Raster(laSpan, loSpan, getTile());
                raster.initializeIndexRaster(getGeoToIndex());
                subTiles.generateSubTileList(laSpan, loSpan);

                raster.isInizialized = true;
            }
        }

        return TileObject.TILE_SIZE * 2;
    }
}
