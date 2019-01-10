package ch.bailu.aat.map.tile;

import org.mapsforge.core.model.Tile;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.util.ui.AppLog;

public class TileObjectCache implements Closeable {


    private final static int INITIAL_CAPACITY = 5;

    private final LockCache<TileObject> tiles = new LockCache<>(INITIAL_CAPACITY);

    public final static TileObjectCache NULL = new TileObjectCache() {
        @Override
        public void put(TileObject t) {

        }
    };


    /**
     * Non synchronized function. This works because LockCache can only grow.
     *
     */
    public boolean isInCache(String string) {
        if (string != null) {
            int hash = string.hashCode();

            for (int i = 0; i < tiles.size(); i++) {
                TileObject o = tiles.get(i);

                if (o != null && hash == tiles.get(i).hashCode()) {
                    return true;
                }
            }

        }
        return false;
    }


    public synchronized TileObject  get(String string) {
        for (int i = 0; i<tiles.size(); i++) {
            if (tiles.get(i).toString().equals(string)) {
                return tiles.use(i);
            }
        }
        return null;
    }


    public synchronized TileObject get(Tile tile) {
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


    public synchronized void put(TileObject handle) {
        tiles.add(handle);
    }

    @Override
    public synchronized void close() {
        tiles.close();
    }


    public synchronized void reDownloadTiles(ServiceContext sc) {
        for (int i = 0; i<tiles.size(); i++) {
            tiles.get(i).reDownload(sc);
        }
    }


    public synchronized void reset() {
        tiles.reset();
    }


    public synchronized void setCapacity(int capacity) {
        tiles.ensureCapacity(capacity);
    }


    public synchronized int size() {
        return tiles.size();
    }


    public synchronized boolean isReadyAndLoaded() {
        for (int i = 0; i<tiles.size(); i++) {
            //AppLog.d(this, tiles.get(i).getID());
            if (tiles.get(i) != null) {
                if (!tiles.get(i).isReadyAndLoaded())
                    return false;
            }
        }
        return true;
    }
}
