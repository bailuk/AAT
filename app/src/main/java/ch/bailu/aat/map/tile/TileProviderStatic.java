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
        return source.getMaximumZoomLevel();
    }

    @Override
    public int getMinimumZoomLevel() {
        return source.getMinimumZoomLevel();
    }


    @Override
    public TileBitmap get(Tile tile) {
        AppLog.d(this, tile.toString());

        final TileObject handle = getTileHandle(tile);

        if (handle != null)
            return handle.getTileBitmap();

        return null;
    }


    private TileObject getTileHandle(Tile tile) {
        TileObject handle = getHandleFromList(tile);

        if (handle == null) {
            handle = loadHandle(tile);

            if (handle != null)
                tiles.add(handle);
        }

        return handle;
    }


    private TileObject getHandleFromList(Tile tile) {
        for (TileObject handle: tiles) {
            if (TileObjectCache.compare(handle.getTile(), tile)) {
                return handle;
            }
        }
        return null;
    }


    private TileObject loadHandle(Tile mapTile) {
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
        return getHandleFromList(tile) != null;
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
        AppLog.d(this, "Ready?: " + tiles.size());

        for (TileObject tile: tiles) {
            if (tile.isReady()==false) return false;
        }
        return true;
    }


    @Override
    public void close() {

        for (TileObject tile: tiles) {
            tile.free();
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
