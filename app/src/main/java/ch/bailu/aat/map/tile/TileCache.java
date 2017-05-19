package ch.bailu.aat.map.tile;

import org.mapsforge.core.model.Tile;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;

public abstract class TileCache<T> implements Closeable {

    public abstract T get(String string);


    public abstract T get(Tile mt);


    public abstract void put(T handle);

    /*
    @Override
    public void close() {}
*/

    public abstract void reDownloadTiles(ServiceContext sc);
    public abstract void reset();
    public abstract void setCapacity(int capacity);

    public abstract int size();// {return 0;}

    //public final static TileCache

    public final static TileCache NULL = new TileCache<Object>() {

        @Override
        public Object get(String string) {
            return null;
        }

        @Override
        public Object get(Tile mt) {
            return null;
        }

        @Override
        public void put(Object handle) {

        }

        @Override
        public void reDownloadTiles(ServiceContext sc) {

        }

        @Override
        public void reset() {

        }

        @Override
        public void setCapacity(int capacity) {

        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public void close() {}
    };
}
