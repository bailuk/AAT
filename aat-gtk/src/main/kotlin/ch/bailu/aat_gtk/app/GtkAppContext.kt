package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.dispatcher.GtkBroadcaster
import ch.bailu.aat_gtk.service.GtkServices
import ch.bailu.aat_gtk.service.location.directory.GtkSummaryConfig
import ch.bailu.aat_gtk.solid.GtkStorage
import ch.bailu.aat_gtk.solid.SolidGtkDataDirectory
import ch.bailu.aat_gtk.util.sql.H2DbConnection
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.TilePainter
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidDem3Directory
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory
import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.DownloadConfig
import ch.bailu.aat_lib.service.directory.MapPreviewInterface
import ch.bailu.aat_lib.service.directory.SummaryConfig
import ch.bailu.aat_lib.util.Timer
import ch.bailu.aat_lib.util.sql.DbConnection
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import ch.bailu.foc.FocFile

object GtkAppContext: AppContext {
    private val focFactory = FocFactory { string: String? -> FocFile(string) }
    private val storage = GtkStorage()
    private val broadcaster = GtkBroadcaster()
    private val solidDataDirectory = SolidGtkDataDirectory(storage, focFactory)
    private val solidSummaryConfig = GtkSummaryConfig()
    private val services = GtkServices(this)


    override fun toFoc(string: String?): Foc {
        return focFactory.toFoc(string)
    }

    override fun getBroadcaster(): Broadcaster {
        return broadcaster
    }

    override fun getServices(): ServicesInterface {
        return services
    }

    override fun getStorage(): StorageInterface {
        return storage
    }

    override fun getSummaryConfig(): SummaryConfig {
        return solidSummaryConfig
    }

    override fun createDataBase(): DbConnection {
        return H2DbConnection()
    }


    private class Preview: MapPreviewInterface {
        override fun isReady(): Boolean {
            return true
        }

        override fun generateBitmapFile() {}

        override fun onDestroy() {}

    }
    override fun createMapPreview(info: GpxInformation?, previewImageFile: Foc?): MapPreviewInterface {
        return Preview()
    }

    override fun createMapTile(): MapTileInterface {
        AppLog.d(this, "Not yet implemented createMapTile")
        TODO("Not yet implemented")
    }

    override fun getDem3Directory(): SolidDem3Directory {
        AppLog.d(this, "Not yet implemented getDem3Directory")
        TODO("Not yet implemented")
    }

    override fun getDownloadConfig(): DownloadConfig {
        AppLog.d(this, "Not yet implemented getDownloadConfig")
        TODO("Not yet implemented")
    }

    override fun getDataDirectory(): SolidDataDirectory {
        return solidDataDirectory
    }

    override fun getAssets(): FocFactory {
        AppLog.d(this, "Not yet implemented getAssets")
        TODO("Not yet implemented")
    }

    override fun getMapDirectory(): SolidMapsForgeDirectory {
        AppLog.d(this, "Not yet implemented getMapDirectory")
        TODO("Not yet implemented")
    }

    override fun getTileCacheDirectory(): SolidTileCacheDirectory {
        AppLog.d(this, "Not yet implemented getTileCacheDirectory")
        TODO("Not yet implemented")
    }

    override fun createTimer(): Timer {
        AppLog.d(this, "Not yet implemented createTimer")
        TODO("Not yet implemented")
    }

    override fun getTilePainter(): TilePainter {
        TODO("Not yet implemented")
    }
}
