package ch.bailu.aat.views.map;

import org.osmdroid.tileprovider.MapTile;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.TileStackObject;

public class TileCache implements Closeable {
    private LockCache<TileStackObject> tiles;


    public TileCache(int capacity) {
        tiles = new LockCache(capacity);
    }


    public TileStackObject get(String string) {
        for (int i = 0; i<tiles.size(); i++) {
            if (tiles.get(i).toString().equals(string)) {
                return tiles.use(i);
            }
        }
        return null;
    }


    public TileStackObject get(MapTile mt) {
        final String mtile = mt.toString();

        for (int i = 0; i<tiles.size(); i++) {
            if (mtile.equals(tiles.get(i).getTile())) {
                return tiles.use(i);
            }
        }
        return null;
    }



    public void put(TileStackObject handle) {
        tiles.add(handle);
    }

    @Override
    public void close() {
        tiles.close();
    }


    public void reDownloadTiles(ServiceContext sc) {
        for (int i = 0; i<tiles.size(); i++) {
            tiles.get(i).reDownload(sc);
        }
    }
    
    
    public void reset() {
        tiles.reset();
    }


    public void setCapacity(int capacity) {
        tiles.ensureCapacity(capacity);
    }
}
