package ch.bailu.aat_lib.service.elevation.loader;

import java.io.Closeable;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.service.elevation.Dem3Status;
import ch.bailu.aat_lib.service.elevation.ElevationProvider;
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile;
import ch.bailu.aat_lib.util.Timer;

public final class Dem3Loader implements Closeable, ElevationProvider {
    private final Dem3TileLoader loader;
    private final Dem3Tiles tiles;

    /**
     * Facade for dem3 tiles
     * Delegates loading of dem3 tiles and returns elevation for a specific coordinate
     * @param appContext Platform context
     * @param timer Platform timer to lazy load dem3 tiles
     * @param tiles Tiles can be shared with elevation updater
     */
    public Dem3Loader(AppContext appContext, Timer timer, Dem3Tiles tiles) {
        this.tiles = tiles;
        this.loader = new Dem3TileLoader(appContext, timer, this.tiles);
    }

    /**
     * Loads and/or downloads dem3 tile that contains a specific coordinate
     * Return elevation for specific coordinate if dem3 tile is loaded
     * @param laE6 latitude E6 format
     * @param loE6 longitude E6 format
     * @return 0 if dem3 tile is not loaded, else altitude for coordinate
     */
    @Override
    public short getElevation(int laE6, int loE6) {
        short result = 0;
        Dem3Coordinates coordinates = new Dem3Coordinates(laE6, loE6);
        Dem3Tile tile = tiles.get(coordinates);

        if (tile == null) {
            loader.loadOrDownloadLater(coordinates);
        } else {
            loader.cancelPending();
            if (tile.getStatus() == Dem3Status.VALID) {
                result = tile.getElevation(laE6, loE6);
            }
        }
        return result;
    }

    /**
     * Load the dem3 tile containing altitude for specific coordinates
     * @param coordinates load tile for this coordinates
     * @return true if is loaded, false if is pending
     */
    public boolean requestDem3Tile(Dem3Coordinates coordinates) {
        Dem3Tile tile = tiles.get(coordinates);

        if (tile == null) {
            tile = loader.loadNow(coordinates);
        }
        return tile != null;
    }

    /**
     * Close the dem3 loader
     */
    @Override
    public void close() {
        loader.close();
    }
}
