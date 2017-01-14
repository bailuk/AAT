package ch.bailu.aat.map.tile;

import org.mapsforge.core.model.Tile;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;

public class TileCache<T> implements Closeable {


    //public static final TileCache NULL = new TileCache<TileStackObject>();


    public T get(String string) {
        return null;
    }


    public T get(Tile mt) {
        return null;
    }


    public void put(T handle) {}

    @Override
    public void close() {}


    public void reDownloadTiles(ServiceContext sc) {}
    public void reset() {}
    public void setCapacity(int capacity) {}

    public int size() {return 0;}
}
