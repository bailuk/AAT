package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.dispatcher.GtkBroadcaster
import ch.bailu.aat_gtk.lib.foc.FocResourceFactory
import ch.bailu.aat_gtk.map.GtkSyncTileBitmap
import ch.bailu.aat_gtk.map.GtkTilePainter
import ch.bailu.aat_gtk.service.GtkServices
import ch.bailu.aat_gtk.service.location.directory.GtkSummaryConfig
import ch.bailu.aat_gtk.solid.*
import ch.bailu.aat_gtk.util.GtkTimer
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
import org.mapsforge.poi.awt.storage.AwtPoiPersistenceManagerFactory
import org.mapsforge.poi.storage.PoiPersistenceManager

object GtkAppContext: AppContext {
    private val focFactory         by lazy { FocFactory { string: String? -> FocFile(string) } }
    private val storage            by lazy { GtkStorage() }
    private val broadcaster        by lazy { GtkBroadcaster() }
    private val solidDataDirectory by lazy { SolidGtkDataDirectory(storage, focFactory) }
    private val solidSummaryConfig by lazy { GtkSummaryConfig() }
    private val solidDem3Directory by lazy { GtkSolidDem3Directory(storage, focFactory) }
    private val services           by lazy { GtkServices(this) }


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
    override fun createMapPreview(info: GpxInformation, previewImageFile: Foc): MapPreviewInterface {
        return Preview()
    }

    override fun createMapTile(): MapTileInterface {
        return GtkSyncTileBitmap()
    }

    override fun getDem3Directory(): SolidDem3Directory {
        return solidDem3Directory
    }

    override fun getDownloadConfig(): DownloadConfig {
        return DownloadConfig(GtkAppConfig)
    }

    override fun getDataDirectory(): SolidDataDirectory {
        return solidDataDirectory
    }

    override fun getAssets(): FocFactory {
        return FocResourceFactory()
    }

    override fun getMapDirectory(): SolidMapsForgeDirectory {
        return GtkMapDirectories(storage, focFactory).createSolidDirectory()
    }

    override fun getTileCacheDirectory(): SolidTileCacheDirectory {
        return GtkSolidTileCacheDirectory(storage, focFactory)
    }

    override fun createTimer(): Timer {
        return GtkTimer()
    }

    override fun getTilePainter(): TilePainter {
        return GtkTilePainter()
    }

    override fun getPoiPersistenceManager(poiDatabase: String): PoiPersistenceManager {
        return AwtPoiPersistenceManagerFactory.getPoiPersistenceManager(poiDatabase)
    }
}
