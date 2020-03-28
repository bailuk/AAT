package ch.bailu.aat.services.cache;

import android.content.Context;
import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.map.tile.source.CacheOnlySource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;

public final class ObjTileCached extends ObjTile {
    private final static int MIN_SAVE_ZOOM_LEVEL = 16;

    private final Tile mapTile;

    private final Obj.Factory cachedFactory, sourceFactory;
    private final String cachedID, sourceID;

    private ObjTile tile = ObjTile.NULL;

    private final SaveTileTask save;

    private final Foc cachedImageFile;

    public ObjTileCached(String id, final ServiceContext sc, Tile t, Source source) {
        super(id);

        mapTile = t;

        sourceID = source.getID(t, sc.getContext());
        sourceFactory = source.getFactory(t);

        final Source cached = new CacheOnlySource(source);

        cachedID = cached.getID(t, sc.getContext());
        cachedFactory = cached.getFactory(t);

        cachedImageFile = FocAndroid.factory(sc.getContext(), cachedID);

        save = new SaveTileTask(sourceID, cachedImageFile);
    }




    @Override
    public void onInsert(ServiceContext sc) {
        if (isLoadable(sc.getContext())) {
            tile = (ObjTile) sc.getCacheService().getObject(cachedID, cachedFactory);
        } else {
            tile = (ObjTile) sc.getCacheService().getObject(sourceID, sourceFactory);
        }
        sc.getCacheService().addToBroadcaster(this);
    }

    private boolean isLoadable(Context c) {
        return cachedImageFile.exists();
    }




    @Override
    public void onChanged(String id, ServiceContext sc) {
        if (id.equals(tile.toString())) {
            AppBroadcaster.broadcast(sc.getContext(),
                    AppBroadcaster.FILE_CHANGED_INCACHE,
                    toString());



            if (
                    mapTile.zoomLevel <= MIN_SAVE_ZOOM_LEVEL &&
                            id.equals(sourceID) &&
                            tile.isLoaded()) {

                sc.getBackgroundService().process(save);
            }
        }
    }

    @Override
    public void access() {
        tile.access();
        super.access();
    }

    @Override
    public void onRemove(ServiceContext cs) {
        tile.free();
    }



    @Override
    public Bitmap getAndroidBitmap() {
        return tile.getAndroidBitmap();
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
    public void reDownload(ServiceContext sc) {
        cachedImageFile.rm();

        tile.free();
        tile = (ObjTile) sc.getCacheService().getObject(sourceID, sourceFactory);
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
    public void onDownloaded(String id, String url, ServiceContext sc) {}


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
        public Obj factory(String id, ServiceContext cs) {
            return new ObjTileCached(id, cs, tile, source);
        }
    }
}
