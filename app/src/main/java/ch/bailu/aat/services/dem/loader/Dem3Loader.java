package ch.bailu.aat.services.dem.loader;

import java.io.Closeable;

import ch.bailu.aat.coordinates.Dem3Coordinates;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.dem.tile.Dem3Status;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.services.dem.tile.ElevationProvider;

public final class Dem3Loader implements Closeable, ElevationProvider {
    private final Dem3TileLoader loader;
    private final Dem3Tiles tiles;

    public Dem3Loader(ServiceContext sc, Dem3Tiles t) {
        tiles = t;
        loader = new Dem3TileLoader(sc, tiles);
    }

    @Override
    public short getElevation(int laE6, int loE6) {
        short r=0;
        Dem3Coordinates c = new Dem3Coordinates(laE6, loE6);
        Dem3Tile t = tiles.get(c);

        if (t == null) {
            loader.loadOrDownloadLater(c);
        } else {
            loader.cancelPending();
            if (t.getStatus() == Dem3Status.VALID) {
                r=t.getElevation(laE6, loE6);
            }
        }
        return r;
    }



    public  Dem3Tile requestDem3Tile(Dem3Coordinates c) {
        Dem3Tile t=tiles.get(c);

        if (t == null) {
            t = loader.loadNow(c);
        }

        return t;
    }

    @Override
    public void close() {
        loader.close();
    }
}
