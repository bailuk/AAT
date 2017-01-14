package ch.bailu.aat.map.tile;

import android.graphics.drawable.Drawable;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.model.common.Observer;

import java.io.Closeable;
import java.util.ArrayList;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.util.ui.AppLog;

public class TileProviderStatic implements TileProviderInterface, Closeable {

    private final ArrayList<TileObject> tiles = new ArrayList<>(10);
    private final TileObject.Source source;

    private final ServiceContext scontext;

    public TileProviderStatic(ServiceContext sc, TileObject.Source s) {
        scontext = sc;
        source = s;

    }


    @Override
    public Drawable getDrawable(Tile tile) {
        return null;
    }

    @Override
    public int getMaximumZoomLevel() {
        return 0;
    }

    @Override
    public int getMinimumZoomLevel() {
        return 0;
    }


    @Override
    public TileBitmap get(Tile tile) {
        final TileObject handle = getTileHandle(tile);

        tiles.add(handle);

        return handle.getTileBitmap();
    }


    private TileObject getTileHandle(Tile tile) {
        TileObject handle = getTileHandleLevel1(tile);


        if (handle == null) {
            handle = getTileHandleLevel2(tile);

            if (handle != null) tiles.add(handle);
        }

        return handle;
    }


    private TileObject getTileHandleLevel1(Tile tile) {
        for (TileObject handle: tiles) {
            if (TileObjectCache.compare(handle.getTile(), tile)) {
                return handle;
            }
        }
        return null;
    }


    private TileObject getTileHandleLevel2(Tile mapTile) {
        if (scontext.isUp()) {
            String id = source.getID(mapTile, scontext.getContext());


            ObjectHandle handle = scontext.getCacheService().getObject(
                    id,
                    source.getFactory(mapTile)
            );


            if (handle instanceof TileObject) {
                AppLog.d(this, id);
                return (TileObject) handle;
            }
        }
        return null;
    }


    @Override
    public boolean contains(Tile tile) {
        return getTileHandleLevel1(tile) != null;
    }

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public int getCapacity() {
        return tiles.size();
    }

    @Override
    public void setCapacity(int numNeeded) {}

    @Override
    public void reDownloadTiles() {}


    public boolean isReady() {

        for (int i=0; i<tiles.size(); i++) {
            if (tiles.get(i).isReady()==false) return false;
        }
        return true;
    }


    @Override
    public void close() {
        for (int i=0; i<tiles.size(); i++) {
            tiles.get(i).free();
        }
        tiles.clear();
    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {
        close();
    }
}
