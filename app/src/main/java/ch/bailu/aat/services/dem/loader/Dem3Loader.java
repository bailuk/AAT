package ch.bailu.aat.services.dem.loader;

import java.io.Closeable;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.dem.tile.Dem3Status;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.services.dem.tile.ElevationProvider;

public class Dem3Loader implements Closeable, ElevationProvider {
    private final Dem3TileLoader loader;
    private final Dem3Tiles tiles;

    public Dem3Loader(ServiceContext sc, Dem3Tiles t) {
        tiles = t;
        loader = new Dem3TileLoader(sc, tiles);
    }

    @Override
    public short getElevation(int laE6, int loE6) {
        short r=0;
        SrtmCoordinates c = new SrtmCoordinates (laE6, loE6);
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



    public  Dem3Tile requestDem3Tile(SrtmCoordinates c) {
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
