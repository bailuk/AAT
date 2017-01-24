package ch.bailu.aat.services.cache;

import android.graphics.Bitmap;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.model.common.Observer;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.graphic.SyncTileBitmap;

public class MapsForgeTileObject extends TileObject implements Observer {
    private final ServiceContext scontext;
    private final Tile tile;

    private final SyncTileBitmap bitmap = new SyncTileBitmap();


    public MapsForgeTileObject(String id, ServiceContext sc, Tile t) {
        super(id);
        scontext = sc;
        tile = t;
        sc.getRenderService().lockToCache(this);
        retreiveBitmap();
    }

    private void retreiveBitmap() {
        TileBitmap b = scontext.getRenderService().getTile(tile);

        if (b != null) {
            bitmap.set(b);
            AppBroadcaster.broadcast(scontext.getContext(),
                    AppBroadcaster.FILE_CHANGED_INCACHE,
                    toString());
        }
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap.getAndroidBitmap();
    }




    @Override
    public void reDownload(ServiceContext sc) {

    }

    @Override
    public boolean isLoaded() {
        return getBitmap() != null;
    }

    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {

    }

    @Override
    public void onChanged(String id, ServiceContext sc) {

    }

    @Override
    public void onChange() {
        retreiveBitmap();
    }


    @Override
    public void onRemove(ServiceContext sc) {
        sc.getRenderService().freeFromCache(this);
        bitmap.free();
        super.onRemove(sc);
    }


    @Override
    public long getSize() {
        return bitmap.getSize();
    }

    @Override
    public TileBitmap getTileBitmap() {
        return bitmap.getTileBitmap();
    }

    public Tile getTile() {
        return tile;
    }


    public static class Factory extends ObjectHandle.Factory {
        private final Tile mapTile;

        public Factory(Tile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return  new MapsForgeTileObject(id, sc, mapTile);
        }

    }



}
