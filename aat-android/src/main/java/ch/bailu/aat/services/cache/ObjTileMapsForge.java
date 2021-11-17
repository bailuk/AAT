package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat_lib.preferences.map.SolidTileSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.service.cache.Obj;

public final class ObjTileMapsForge extends ObjTile {
    private static long DEFAULT_SIZE = SolidTileSize.DEFAULT_TILESIZE_BYTES * 4;

    private final ServiceContext scontext;
    private final Tile tile;

    private final SyncTileBitmap bitmap = new SyncTileBitmap();

    private final String themeID;

    public ObjTileMapsForge(String id, ServiceContext sc, Tile t, String tID) {
        super(id);
        scontext = sc;
        tile = t;
        themeID = tID;

        sc.getRenderService().lockToRenderer(this);
    }


    public String getThemeID() {
        return themeID;
    }


    @Override
    public Bitmap getAndroidBitmap() {
            return bitmap.getAndroidBitmap();
    }


    @Override
    public void reDownload(ServiceContext sc) {}

    @Override
    public boolean isLoaded() {
        return bitmap.getAndroidBitmap() != null;
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}

    @Override
    public void onChanged(String id, ServiceContext sc) {}


    public void onRendered(TileBitmap fromRenderer) {
            bitmap.set(fromRenderer);
            OldAppBroadcaster.broadcast(scontext.getContext(),
                    AppBroadcaster.FILE_CHANGED_INCACHE,
                    getID());
    }


    @Override
    public void onRemove(ServiceContext sc) {
        scontext.getRenderService().freeFromRenderer(ObjTileMapsForge.this);
        bitmap.free();
        super.onRemove(sc);
    }


    @Override
    public long getSize() {
        DEFAULT_SIZE = getSize(bitmap, DEFAULT_SIZE);

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
        public Obj factory(String id, ServiceContext sc) {
            return  new ObjTileMapsForge(id, sc, mapTile, themeID);
        }

    }
}
