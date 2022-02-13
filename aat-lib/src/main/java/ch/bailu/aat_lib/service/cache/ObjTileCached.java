package ch.bailu.aat_lib.service.cache;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.map.tile.source.CacheOnlySource;
import ch.bailu.aat_lib.map.tile.source.Source;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.foc.Foc;

public final class ObjTileCached extends ObjTile {
    private final static int MIN_SAVE_ZOOM_LEVEL = 16;

    private final Tile mapTile;

    private final Obj.Factory cachedFactory, sourceFactory;
    private final String cachedID, sourceID;

    private ObjTile tile = ObjTile.NULL;

    private final SaveTileTask save;

    private final Foc cachedImageFile;

    public ObjTileCached(String id, final AppContext sc, Tile t, Source source) {
        super(id);

        mapTile = t;

        sourceID = source.getID(t, sc);
        sourceFactory = source.getFactory(t);

        final Source cached = new CacheOnlySource(source);

        cachedID = cached.getID(t, sc);
        cachedFactory = cached.getFactory(t);

        cachedImageFile = sc.toFoc(cachedID);

        save = new SaveTileTask(sourceID, cachedImageFile);
    }




    @Override
    public void onInsert(AppContext sc) {
        if (isLoadable()) {
            tile = (ObjTile) sc.getServices().getCacheService().getObject(cachedID, cachedFactory);
        } else {
            tile = (ObjTile) sc.getServices().getCacheService().getObject(sourceID, sourceFactory);
        }
        sc.getServices().getCacheService().addToBroadcaster(this);
    }

    private boolean isLoadable() {
        return cachedImageFile.exists();
    }




    @Override
    public void onChanged(String id, AppContext sc) {
        if (id.equals(tile.toString())) {
            sc.getBroadcaster().broadcast(
                    AppBroadcaster.FILE_CHANGED_INCACHE,
                    toString());



            if (
                    mapTile.zoomLevel <= MIN_SAVE_ZOOM_LEVEL &&
                            id.equals(sourceID) &&
                            tile.isLoaded()) {

                sc.getServices().getBackgroundService().process(save);
            }
        }
    }

    @Override
    public void access() {
        tile.access();
        super.access();
    }

    @Override
    public void onRemove(AppContext cs) {
        tile.free();
    }





    @Override
    public TileBitmap getTileBitmap() {
        return tile.getTileBitmap();
    }

    @Override
    public Tile getTile() {
        return mapTile;
    }

    @Override
    public void reDownload(AppContext sc) {
        cachedImageFile.rm();

        tile.free();
        tile = (ObjTile) sc.getServices().getCacheService().getObject(sourceID, sourceFactory);
    }

    @Override
    public boolean isLoaded() {
        return tile.isLoaded();
    }


    @Override
    public long getSize() {
        return MIN_SIZE;
    }

    @Override
    public void onDownloaded(String id, String url, AppContext sc) {}


    @Override
    public Foc getFile() {
        return cachedImageFile;
    }


    public static class Factory extends Obj.Factory {
        private final Source source;
        private final Tile tile;

        public Factory(Tile t, Source s) {
            source = s;
            tile = t;
        }

        @Override
        public Obj factory(String id, AppContext cs) {
            return new ObjTileCached(id, cs, tile, source);
        }
    }
}
