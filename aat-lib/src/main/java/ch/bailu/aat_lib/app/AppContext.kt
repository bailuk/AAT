package ch.bailu.aat_lib.app

import ch.bailu.aat_lib.broadcaster.Broadcaster
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
import org.mapsforge.poi.storage.PoiPersistenceManager

interface AppContext : FocFactory {
    val broadcaster: Broadcaster
    val services: ServicesInterface
    val storage: StorageInterface
    val summaryConfig: SummaryConfig
    fun createDataBase(): DbConnection
    fun createMapPreview(info: GpxInformation, previewImageFile: Foc): MapPreviewInterface
    fun createMapTile(): MapTileInterface
    val dem3Directory: SolidDem3Directory
    val downloadConfig: DownloadConfig
    val dataDirectory: SolidDataDirectory
    val assets: FocFactory
    val mapDirectory: SolidMapsForgeDirectory
    val tileCacheDirectory: SolidTileCacheDirectory
    fun createTimer(): Timer
    val tilePainter: TilePainter
    fun getPoiPersistenceManager(poiDatabase: String): PoiPersistenceManager
}
