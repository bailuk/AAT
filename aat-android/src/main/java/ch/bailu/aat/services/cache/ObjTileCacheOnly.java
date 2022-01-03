package ch.bailu.aat.services.cache;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.map.tile.TileFlags;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.preferences.map.SolidTileSize;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.FileTask;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjTile;
import ch.bailu.aat_lib.service.cache.OnObject;
import ch.bailu.aat_lib.util.Objects;
import ch.bailu.foc.Foc;

public class ObjTileCacheOnly extends ObjTile {

    private final Foc file;
    private final Tile tile;
    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private final Source source;



    public ObjTileCacheOnly(String id, AppContext sc, Tile t, Source s) {
        super(id);
        tile = t;
        file = sc.toFoc(id);

        source = s;

        sc.getServices().getCacheService().addToBroadcaster(this);

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
    public void reDownload(AppContext sc) {

    }


    @Override
    public void onInsert(AppContext sc) {
        if (isLoadable()) load(sc.getServices());
    }


    public void onRemove(AppContext sc) {
        super.onRemove(sc);
        bitmap.free();
    }


    @Override
    public boolean isLoaded() {
        return bitmap.isLoaded();
    }


    protected boolean isLoadable() {
        file.update();
        return file.isFile() && file.canRead();
    }


    @Override
    public void onDownloaded(String id, String u, AppContext sc) {
        if (Objects.equals(id, getID()) && isLoadable()) {
            load(sc.getServices());
        }
    }


    protected boolean fileExists() {
        file.update();
        return getFile().exists();
    }

    protected void load(ServicesInterface sc) {
        sc.getBackgroundService().process(new TileLoaderTask(file));
    }


    public boolean isReadyAndLoaded() {
        boolean loaded = isLoaded();
        boolean notLoadable = isLoadable() == false;

        return loaded || notLoadable;
    }


    @Override
    public void onChanged(String id, AppContext sc) {}



    @Override
    public long getSize() {
        return bitmap.getSize();
    }



    @Override
    public Foc getFile() {
        return file;
    }

    private static class TileLoaderTask extends FileTask {

        public TileLoaderTask(Foc f) {
            super(f);
        }

        @Override
        public long bgOnProcess(final AppContext sc) {
            final long[] size = {0};


            new OnObject(sc, getFile().toString(), ObjTileCacheOnly.class) {
                @Override
                public void run(Obj handle) {
                    ObjTileCacheOnly tile = (ObjTileCacheOnly) handle;

                    tile.bitmap.set(
                            getFile(),
                            SolidTileSize.DEFAULT_TILESIZE,
                            TileFlags.ALWAYS_TRANSPARENT || tile.source.isTransparent());


                    sc.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_INCACHE,
                            getFile().toString());

                    size[0] =  tile.bitmap.getSize();


                }
            };
            return size[0];


        }
    }


    public static class Factory extends Obj.Factory {
        private final Tile tile;
        private final Source source;


        public Factory(Tile t, Source s) {
            tile = t;
            source = s;
        }

        @Override
        public Obj factory(String id, AppContext cs) {
            return new ObjTileCacheOnly(id, cs, tile, source);
        }
    }
}

