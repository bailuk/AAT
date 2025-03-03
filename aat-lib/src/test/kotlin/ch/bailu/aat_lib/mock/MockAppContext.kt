package ch.bailu.aat_lib.mock

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.gpx.information.GpxInformation
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
import ch.bailu.foc.FocName
import org.mapsforge.poi.storage.PoiPersistenceManager

class MockAppContext : AppContext {
    override val broadcaster: Broadcaster = MockBroadcaster()

    override val services: ServicesInterface = MockServices()

    override val storage: StorageInterface
        get() = TODO("Not yet implemented")
    override val summaryConfig: SummaryConfig
        get() = TODO("Not yet implemented")

    override fun createDataBase(): DbConnection {
        TODO("Not yet implemented")
    }

    override fun createMapPreview(
        info: GpxInformation,
        previewImageFile: Foc
    ): MapPreviewInterface {
        TODO("Not yet implemented")
    }

    override fun createMapTile(): MapTileInterface {
        TODO("Not yet implemented")
    }

    override val dem3Directory: SolidDem3Directory
        get() = TODO("Not yet implemented")
    override val downloadConfig: DownloadConfig
        get() = TODO("Not yet implemented")
    override val dataDirectory: SolidDataDirectory
        get() = SolidDataDirectory(MockSolidDataDirectory(MockStorage(), this), this)
    override val assets: FocFactory
        get() = TODO("Not yet implemented")
    override val mapDirectory: SolidMapsForgeDirectory
        get() = TODO("Not yet implemented")
    override val tileCacheDirectory: SolidTileCacheDirectory
        get() = TODO("Not yet implemented")

    override fun createTimer(): Timer {
        TODO("Not yet implemented")
    }

    override val tilePainter: TilePainter
        get() = TODO("Not yet implemented")

    override fun getPoiPersistenceManager(poiDatabase: String): PoiPersistenceManager {
        TODO("Not yet implemented")
    }

    override fun toFoc(string: String?): Foc {
        return FocName(string)
    }
}
