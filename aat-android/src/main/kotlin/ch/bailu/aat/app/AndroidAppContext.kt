package ch.bailu.aat.app

import android.content.Context
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat.map.AndroidTilePainter
import ch.bailu.aat.map.mapsforge.MapsForgePreview
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.map.AndroidMapDirectories
import ch.bailu.aat.preferences.map.AndroidSolidDem3Directory
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectoryDefault
import ch.bailu.aat.services.directory.AndroidSummaryConfig
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat.util.graphic.AndroidSyncTileBitmap
import ch.bailu.aat.util.sql.AndroidDbConnection
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxInformation
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
import ch.bailu.foc_android.FocAndroidFactory
import ch.bailu.foc_android.FocAssetFactory
import org.mapsforge.poi.android.storage.AndroidPoiPersistenceManagerFactory
import org.mapsforge.poi.storage.PoiPersistenceManager

class AndroidAppContext(private val context: Context, override val services: ServicesInterface) : AppContext {
    override val broadcaster = AndroidBroadcaster(context)
    override val assets = FocAssetFactory(context)


    override val storage: StorageInterface
        get() = Storage(context)

    override val summaryConfig: SummaryConfig
        get() =  AndroidSummaryConfig(context)

    override fun createDataBase(): DbConnection {
        return AndroidDbConnection(context)
    }

    override fun createMapPreview(info: GpxInformation, previewImageFile: Foc): MapPreviewInterface {
        return MapsForgePreview(context, this, info, previewImageFile)
    }

    override fun createMapTile(): MapTileInterface {
        return AndroidSyncTileBitmap()
    }

    override val dem3Directory: SolidDem3Directory
        get() = AndroidSolidDem3Directory(context)

    override val downloadConfig: DownloadConfig
    get() = DownloadConfig(AndroidAppConfig())


    override val dataDirectory: SolidDataDirectory
        get() =  SolidDataDirectory(AndroidSolidDataDirectoryDefault(context), FocAndroidFactory(context))


    override val mapDirectory: SolidMapsForgeDirectory
        get() =  AndroidMapDirectories(context).createSolidDirectory()


    override val tileCacheDirectory: SolidTileCacheDirectory
        get() =  AndroidSolidTileCacheDirectory(context)

    override fun createTimer(): Timer {
        return AndroidTimer()
    }

    override val tilePainter: TilePainter
        get() = AndroidTilePainter()


    override fun getPoiPersistenceManager(poiDatabase: String): PoiPersistenceManager {
        return AndroidPoiPersistenceManagerFactory.getPoiPersistenceManager(poiDatabase)
    }

    override fun toFoc(string: String): Foc {
        return FocAndroidFactory(context).toFoc(string)
    }
}
