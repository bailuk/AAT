package ch.bailu.aat_lib.service.elevation.loader

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastData.getFile
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.preferences.map.SolidDem3EnableDownload
import ch.bailu.aat_lib.service.background.DownloadTask
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile
import ch.bailu.aat_lib.util.Timer
import java.io.Closeable


class Dem3TileLoader(
    private val appContext: AppContext,
    private val timer: Timer,
    private val tiles: Dem3Tiles
) :
    Closeable {
    private var pending: Dem3Coordinates? = null

    private val sdownload = SolidDem3EnableDownload(appContext.storage)

    private val timeout = Runnable {
        if (havePending()) {
            loadOrDownloadPending()
            stopTimer()
        }
    }

    private val onFileDownloaded =
        BroadcastReceiver { args ->
            val id = getFile(args)
            val tile = tiles.get(id)
            tile?.reload(appContext.services, appContext.dem3Directory)
        }

    init {
        appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_ONDISK, onFileDownloaded)
    }

    private fun loadOrDownloadPending() {
        val toLoad = pending
        pending = null

        if (toLoad is Dem3Coordinates) {
            loadNow(toLoad)

            if (sdownload.value) {
                downloadNow(toLoad)
            }
        }
    }

    fun loadNow(coordinates: Dem3Coordinates): Dem3Tile? {
        if (havePending()) cancelPending()
        return loadIntoOldestSlot(coordinates)
    }

    private fun loadIntoOldestSlot(coordinates: Dem3Coordinates): Dem3Tile? {
        if (!tiles.have(coordinates)) {
            val slot = tiles.oldestProcessed

            if (slot != null && !slot.isLocked) {
                slot.load(appContext.services, coordinates, appContext.dem3Directory)
            }
        }

        return tiles.get(coordinates)
    }

    fun loadOrDownloadLater(coordinates: Dem3Coordinates?) {
        if (pending == null) { // first BitmapRequest
            startTimer()
        }
        pending = coordinates
    }

    fun cancelPending() {
        pending = null
        stopTimer()
    }

    private fun havePending(): Boolean {
        return pending != null
    }

    private fun startTimer() {
        timer.cancel()
        timer.kick(MILLIS, timeout)
    }

    private fun stopTimer() {
        timer.cancel()
    }

    private fun downloadNow(c: Dem3Coordinates) {
        val file = appContext.dem3Directory.toFile(c)
        if (!file.exists()) {
            val handle = DownloadTask(c.toURL(), file, appContext.downloadConfig)
            appContext.services.getBackgroundService().process(handle)
        }
    }

    override fun close() {
        appContext.broadcaster.unregister(onFileDownloaded)
    }

    companion object {
        private const val MILLIS: Long = 2000
    }
}
