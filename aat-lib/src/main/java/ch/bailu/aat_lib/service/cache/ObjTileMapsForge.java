package ch.bailu.aat_lib.service.cache;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.map.tile.MapTileInterface;
import ch.bailu.aat_lib.preferences.map.SolidTileSize;

public final class ObjTileMapsForge extends ObjTile {
    private static long DEFAULT_SIZE = SolidTileSize.DEFAULT_TILESIZE_BYTES * 4;

    private final AppContext appContext;
    private final Tile tile;

    private final MapTileInterface bitmap;

    private final String themeID;

    public ObjTileMapsForge(String id, AppContext appContext, Tile t, String tID) {
        super(id);
        this.appContext = appContext;
        tile = t;
        themeID = tID;

        bitmap = appContext.createMapTile();

        this.appContext.getServices().getRenderService().lockToRenderer(this);
    }


    public String getThemeID() {
        return themeID;
    }


    @Override
    public void reDownload(AppContext sc) {}

    @Override
    public boolean isLoaded() {
        return bitmap.isLoaded();
    }

    @Override
    public void onDownloaded(String id, String url, AppContext sc) {}

    @Override
    public void onChanged(String id, AppContext sc) {}


    public void onRendered(TileBitmap fromRenderer) {
            bitmap.set(fromRenderer);
            appContext.getBroadcaster().broadcast(
                    AppBroadcaster.FILE_CHANGED_INCACHE,
                    getID());
    }


    @Override
    public void onRemove(AppContext sc) {
        appContext.getServices().getRenderService().freeFromRenderer(ObjTileMapsForge.this);
        bitmap.free();
        super.onRemove(sc);
    }


    @Override
    public long getSize() {
        DEFAULT_SIZE = ObjTile.getSize(bitmap, DEFAULT_SIZE);

        if (isLoaded()) {
            return DEFAULT_SIZE;
        } else {
            return DEFAULT_SIZE * 4;
        }
    }

    @Override
    public TileBitmap getTileBitmap() {
        return bitmap.getTileBitmap();
    }

    public Tile getTile() {
        return tile;
    }



    public static class Factory extends Obj.Factory {
        private final Tile mapTile;
        private final String themeID;

        public Factory(Tile t, String tID) {

            themeID = tID;
            mapTile=t;
        }

        @Override
        public Obj factory(String id, AppContext appContext) {
            return  new ObjTileMapsForge(id, appContext, mapTile, themeID);
        }

    }
}
