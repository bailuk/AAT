package ch.bailu.aat_lib.service.elevation.loader

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.elevation.Dem3Status
import ch.bailu.aat_lib.service.elevation.ElevationProvider
import ch.bailu.aat_lib.util.Timer
import java.io.Closeable

class Dem3Loader(appContext: AppContext, timer: Timer, private val tiles: Dem3Tiles) :
    Closeable, ElevationProvider {
    private val loader = Dem3TileLoader(appContext, timer, this.tiles)

    /**
     * Loads and/or downloads dem3 tile that contains a specific coordinate
     * Return elevation for specific coordinate if dem3 tile is loaded
     * @param laE6 latitude E6 format
     * @param loE6 longitude E6 format
     * @return 0 if dem3 tile is not loaded, else altitude for coordinate
     */
    override fun getElevation(laE6: Int, loE6: Int): Short {
        var result: Short = 0
        val coordinates = Dem3Coordinates(laE6, loE6)
        val tile = tiles.get(coordinates)

        if (tile == null) {
            loader.loadOrDownloadLater(coordinates)
        } else {
            loader.cancelPending()
            if (tile.getStatus() == Dem3Status.VALID) {
                result = tile.getElevation(laE6, loE6)
            }
        }
        return result
    }

    /**
     * Load the dem3 tile containing altitude for specific coordinates
     * @param coordinates load tile for this coordinates
     * @return true if is loaded, false if is pending
     */
    fun requestDem3Tile(coordinates: Dem3Coordinates): Boolean {
        var tile = tiles.get(coordinates)

        if (tile == null) {
            tile = loader.loadNow(coordinates)
        }
        return tile != null
    }

    /**
     * Close the dem3 loader
     */
    override fun close() {
        loader.close()
    }
}
