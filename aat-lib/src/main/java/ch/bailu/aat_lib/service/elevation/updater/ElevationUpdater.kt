package ch.bailu.aat_lib.service.elevation.updater

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.elevation.Dem3Status
import ch.bailu.aat_lib.service.elevation.loader.Dem3Loader
import ch.bailu.aat_lib.service.elevation.loader.Dem3Tiles
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile
import java.io.Closeable


class ElevationUpdater(
    private val appContext: AppContext,
    private val loader: Dem3Loader,
    private val tiles: Dem3Tiles
) :
    Closeable {
    private val pendingUpdates = PendingUpdatesMap()

    private val onFileChanged =
        BroadcastReceiver { args ->
            val id = args[0]
            synchronized(this@ElevationUpdater) {
                if (tiles.have(id)) {
                    requestElevationUpdates()
                }
            }
        }

    init {
        appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onFileChanged)
    }

    @Synchronized
    fun requestElevationUpdates(
        e: ElevationUpdaterClient,
        coordinates: List<Dem3Coordinates>
    ) {
        for (c in coordinates) {
            addObject(c, e)
        }

        requestElevationUpdates()
    }

    @Synchronized
    fun requestElevationUpdates() {
        updateClients()
        loadTiles()
    }

    @Synchronized
    fun cancelElevationUpdates(e: ElevationUpdaterClient) {
        pendingUpdates.remove(e)
    }

    private fun addObject(c: Dem3Coordinates, e: ElevationUpdaterClient) {
        pendingUpdates.add(c, e)
    }

    private fun loadTiles() {
        val coordinates = pendingUpdates.coordinates()
        while (coordinates.hasNext() && loader.requestDem3Tile(coordinates.next()));
    }

    private fun updateClients() {
        var t = 0
        do {
            val tile = tiles.get(t++)
            if (tile is Dem3Tile) {
                updateClients(tile)
            }
        } while(tile is Dem3Tile)
    }

    private fun updateClients(tile: Dem3Tile) {
        tile.lock()

        if (tile.getStatus() == Dem3Status.VALID) {
            val elevationUpdaterClients = pendingUpdates.get(tile.getCoordinates())

            if (elevationUpdaterClients != null) {
                for (e in elevationUpdaterClients) {
                    e.updateFromSrtmTile(appContext, tile)
                }
            }
        }

        if (tile.getStatus() == Dem3Status.VALID || tile.getStatus() == Dem3Status.EMPTY) {
            pendingUpdates.remove(tile.getCoordinates())
        }

        tile.free()
    }

    override fun close() {
        appContext.broadcaster.unregister(onFileChanged)
    }
}
