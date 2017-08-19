package ch.bailu.aat.map.tile;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.util.ui.AppLog;

public class TileObjectCache extends TileCache<TileObject> {


    private final static int INITIAL_CAPACITY = 5;


    private final LockCache<TileObject> tiles = new LockCache(INITIAL_CAPACITY);

    @Override
    public TileObject get(String string) {
        for (int i = 0; i<tiles.size(); i++) {
            if (tiles.get(i).toString().equals(string)) {
                return tiles.use(i);
            }
        }
        return null;
    }

    @Override
    public TileObject get(Tile tile) {
        for (int i = 0; i<tiles.size(); i++) {
            if (compare(tile, tiles.get(i).getTile())) {
                return tiles.use(i);
            }
        }
        return null;
    }

    public static boolean compare(Tile a, Tile b) {
        return a.tileX == b.tileX && a.tileY == b.tileY && a.zoomLevel == b.zoomLevel;
    }

    @Override
    public void put(TileObject handle) {
        tiles.add(handle);
    }

    @Override
    public void close() {
        tiles.close();
    }

    @Override
    public void reDownloadTiles(ServiceContext sc) {
        for (int i = 0; i<tiles.size(); i++) {
            tiles.get(i).reDownload(sc);
        }
    }

    @Override
    public void reset() {
        tiles.reset();
    }

    @Override
    public void setCapacity(int capacity) {
        tiles.ensureCapacity(capacity);
    }

    @Override
    public int size() {
        return tiles.size();
    }


    @Override
    public boolean isReadyAndLoaded() {

        AppLog.d(this, "Ready and loaded: " + tiles.size());
        for (int i = 0; i<tiles.size(); i++) {
            AppLog.d(this, tiles.get(i).getID());
            if (tiles.get(i) != null) {
                if (!tiles.get(i).isReadyAndLoaded())
                    return false;
            }
        }
        return true;
    }
}
