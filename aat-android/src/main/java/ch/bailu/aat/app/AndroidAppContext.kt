package ch.bailu.aat.app

import android.content.Context
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat.map.mapsforge.MapsForgePreview
import ch.bailu.aat.map.AndroidTilePainter
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.map.AndroidMapDirectories
import ch.bailu.aat.preferences.map.AndroidSolidDem3Directory
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory
import ch.bailu.aat.services.directory.AndroidSummaryConfig
import ch.bailu.aat.util.AndroidTimer
import ch.bailu.aat.util.graphic.AndroidSyncTileBitmap
import ch.bailu.aat.util.sql.AndroidDbConnection
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.Broadcaster
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
import ch.bailu.foc.FocFactory
import ch.bailu.foc_android.FocAndroidFactory
import ch.bailu.foc_android.FocAssetFactory
import org.mapsforge.poi.android.storage.AndroidPoiPersistenceManagerFactory
import org.mapsforge.poi.storage.PoiPersistenceManager
import javax.annotation.Nonnull

class AndroidAppContext(private val context: Context, private val services: ServicesInterface) : AppContext {
    private val broadcaster = AndroidBroadcaster(context)
    private val assets = FocAssetFactory(context)

    override fun getBroadcaster(): Broadcaster {
        return broadcaster
    }

    override fun getServices(): ServicesInterface {
        return services
    }

    override fun getStorage(): StorageInterface {
        return Storage(context)
    }

    override fun getSummaryConfig(): SummaryConfig {
        return AndroidSummaryConfig(context)
    }

    override fun createDataBase(): DbConnection {
        return AndroidDbConnection(context)
    }

    override fun createMapPreview(
        @Nonnull info: GpxInformation,
        @Nonnull previewImageFile: Foc
    ): MapPreviewInterface {
        return MapsForgePreview(context, this, info, previewImageFile)
    }

    override fun createMapTile(): MapTileInterface {
        return AndroidSyncTileBitmap()
    }

    override fun getDem3Directory(): SolidDem3Directory {
        return AndroidSolidDem3Directory(context)
    }

    override fun getDownloadConfig(): DownloadConfig {
        return DownloadConfig(AndroidAppConfig())
    }

    override fun getDataDirectory(): SolidDataDirectory {
        return AndroidSolidDataDirectory(context)
    }

    override fun getAssets(): FocFactory {
        return assets
    }

    override fun getMapDirectory(): SolidMapsForgeDirectory {
        return AndroidMapDirectories(context).createSolidDirectory()
    }

    override fun getTileCacheDirectory(): SolidTileCacheDirectory {
        return AndroidSolidTileCacheDirectory(context)
    }

    override fun createTimer(): Timer {
        return AndroidTimer()
    }

    override fun getTilePainter(): TilePainter {
        return AndroidTilePainter()
    }

    override fun getPoiPersistenceManager(@Nonnull poiDatabase: String): PoiPersistenceManager {
        return AndroidPoiPersistenceManagerFactory.getPoiPersistenceManager(poiDatabase)
    }

    override fun toFoc(string: String): Foc {
        return FocAndroidFactory(context).toFoc(string)
    }
}
