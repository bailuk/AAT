package ch.bailu.aat.map.tile;

import org.mapsforge.core.model.Tile;
import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.LockCache;
import ch.bailu.aat.services.cache.TileObject;

public class TileObjectCache extends TileCache<TileObject> {
    private final static int INITIAL_CAPACITY = 5;

    private final LockCache<TileObject> tiles = new LockCache(INITIAL_CAPACITY);

    public static final TileCache<TileObject> NULL_TILE_OBJECT_CACHE
            = new TileCache<TileObject>() {};


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
    public TileObject get(Tile mt) {
        for (int i = 0; i<tiles.size(); i++) {
            if (compare(mt, tiles.get(i).getTile())) {
                return tiles.use(i);
            }
        }
        return null;
    }

    public static boolean compare(MapTile a, Tile b) {
        return a.getX() == b.tileX && a.getY() == b.tileY && a.getZoomLevel() == b.zoomLevel;
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


}
