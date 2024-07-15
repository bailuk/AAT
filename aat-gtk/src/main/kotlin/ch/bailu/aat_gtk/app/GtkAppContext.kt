package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.dispatcher.GtkBroadcaster
import ch.bailu.aat_gtk.map.GtkSyncTileBitmap
import ch.bailu.aat_gtk.map.GtkTilePainter
import ch.bailu.aat_gtk.service.GtkServices
import ch.bailu.aat_gtk.service.location.directory.GtkSummaryConfig
import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.solid.GtkSolidDem3Directory
import ch.bailu.aat_gtk.solid.GtkSolidTileCacheDirectory
import ch.bailu.aat_gtk.solid.GtkStorage
import ch.bailu.aat_gtk.solid.SolidGtkDefaultDirectory
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_gtk.util.sql.H2DbConnection
import ch.bailu.aat_gtk.view.map.preview.MapsForgePreview
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.service.background.DownloadConfig
import ch.bailu.aat_lib.service.directory.MapPreviewInterface
import ch.bailu.aat_lib.util.Timer
import ch.bailu.aat_lib.util.sql.DbConnection
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import ch.bailu.foc.FocFile
import ch.bailu.foc_extended.FocResourceFactory
import org.mapsforge.poi.awt.storage.AwtPoiPersistenceManagerFactory
import org.mapsforge.poi.storage.PoiPersistenceManager

object GtkAppContext: AppContext {
    private val focFactory          by lazy { FocFactory { string: String -> FocFile(string) } }
    override val storage            by lazy { GtkStorage() }
    override val broadcaster        by lazy { GtkBroadcaster() }
    override val dataDirectory      by lazy { SolidDataDirectory(SolidGtkDefaultDirectory(storage, focFactory), focFactory) }
    override val summaryConfig      by lazy { GtkSummaryConfig() }
    override val dem3Directory      by lazy { GtkSolidDem3Directory(storage, focFactory) }
    override val services           by lazy { GtkServices(this) }
    override val downloadConfig     by lazy { DownloadConfig(GtkAppConfig) }
    override val assets             by lazy { FocResourceFactory() }
    override val mapDirectory       by lazy { GtkMapDirectories(storage, focFactory).createSolidDirectory() }
    override val tileCacheDirectory by lazy { GtkSolidTileCacheDirectory(storage, focFactory) }
    override val tilePainter        by lazy { GtkTilePainter() }

    override fun toFoc(string: String): Foc {
        return focFactory.toFoc(string)
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
        return MapsForgePreview(this, info, previewImageFile)
    }

    override fun createMapTile(): MapTileInterface {
        return GtkSyncTileBitmap()
    }

    override fun createTimer(): Timer {
        return GtkTimer()
    }

    override fun getPoiPersistenceManager(poiDatabase: String): PoiPersistenceManager {
        return AwtPoiPersistenceManagerFactory.getPoiPersistenceManager(poiDatabase)
    }
}
