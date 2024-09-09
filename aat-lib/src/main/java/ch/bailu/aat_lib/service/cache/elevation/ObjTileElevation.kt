package ch.bailu.aat_lib.service.cache.elevation;


import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.map.tile.MapTileInterface;
import ch.bailu.aat_lib.preferences.map.SolidTileSize;
import ch.bailu.aat_lib.service.cache.ObjTile;
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile;
import ch.bailu.aat_lib.service.elevation.tile.DemDimension;
import ch.bailu.aat_lib.service.elevation.tile.DemGeoToIndex;
import ch.bailu.aat_lib.service.elevation.tile.DemProvider;
import ch.bailu.aat_lib.service.elevation.tile.DemSplitter;
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient;
import ch.bailu.aat_lib.util.Rect;

public abstract class ObjTileElevation extends ObjTile implements ElevationUpdaterClient {

    private final Tile mapTile;
    private final int split;

    private final MapTileInterface bitmap;

    private final SubTiles subTiles = new SubTiles();
    private final Raster raster = new Raster();



    public ObjTileElevation(String id, MapTileInterface bitmap, Tile _map_tile, int _split) {
        super(id);
        mapTile = _map_tile;
        split = _split;
        this.bitmap = bitmap;
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
        int i = split;
        while (i > 0) {
            dem = factorySplitter(dem);
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
        DemDimension dim = split(Dem3Tile.NULL).getDim();
        return factoryGeoToIndex(dim);
    }


    public abstract void fillBuffer(
            int[] bitmap,
            Raster raster,
            SubTile span,
            DemProvider demtile);


    @Override
    public void onInsert(AppContext appContext) {
        appContext.getServices().getCacheService().addToBroadcaster(this);

        if (!raster.isInitialized()) {
            appContext.getServices().getBackgroundService().process(new RasterInitializer(getID()));
        }
    }


    public void onRemove(AppContext appContext) {
        appContext.getServices().getElevationService().cancelElevationUpdates(this);

        super.onRemove(appContext);
        bitmap.free();
    }

    @Override
    public void onChanged(String id, AppContext appContext) {}

    @Override
    public void onDownloaded(String id, String url, AppContext appContext) {
        if (subTiles.haveID(url)) {
            requestElevationUpdates(appContext);
        }
    }

    @Override
    public long getSize() {
        return bitmap.getSize();
    }


    public boolean isReadyAndLoaded() { // isDisplayable()
        return isInitialized() && subTiles.isNotPainting();
    }


    @Override
    public boolean isLoaded() { // isCacheable()
        return isInitialized() && subTiles.areAllPainted();
    }



    @Override
    public void updateFromSrtmTile(AppContext appContext, Dem3Tile tile) {
        appContext.getServices().getBackgroundService().process(new SubTilePainter(appContext.getServices(), getID(), tile));
    }


    @Override
    public void reDownload(AppContext sc) {

    }

    public boolean isInitialized() {
        return raster.isInitialized();
    }

    public long bgOnProcessPainter(Dem3Tile dem3Tile) {
        long size = 0;

        if (isInitialized()) {

            final SubTile subTile = subTiles.take(dem3Tile.getCoordinates());

            if (subTile != null) {

                size = paintSubTile(subTile, dem3Tile);

                subTiles.done();
            }
        }

        return size;
    }


    private long paintSubTile(SubTile subTile, Dem3Tile dem3Tile) {

        final Rect interR = subTile.toRect();
        final int[] buffer = new int[interR.width() * interR.height()];
        fillBuffer(buffer, raster, subTile, split(dem3Tile));

        bitmap.setBuffer(buffer, interR);

        return (long) interR.width() * interR.height() * 2;
    }

    public long bgOnProcessInitializer(AppContext a) {

        raster.initialize(getTile(), getGeoToIndex(), subTiles);
        requestElevationUpdates(a);

        return SolidTileSize.DEFAULT_TILESIZE * 2;
    }

    public void requestElevationUpdates(AppContext appContext) {
        if (isInitialized())
            appContext.getServices().getElevationService().requestElevationUpdates(this, subTiles.toSrtmCoordinates());
    }
}
