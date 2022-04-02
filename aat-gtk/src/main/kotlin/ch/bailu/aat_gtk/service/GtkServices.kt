package ch.bailu.aat_gtk.service

import ch.bailu.aat_gtk.solid.GtkSolidLocationProvider
import ch.bailu.aat_gtk.solid.SolidGtkDataDirectory
import ch.bailu.aat_gtk.view.GtkStatusIcon
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.service.IconMapServiceInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.BackgroundService
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface
import ch.bailu.aat_lib.service.cache.CacheService
import ch.bailu.aat_lib.service.cache.CacheServiceInterface
import ch.bailu.aat_lib.service.cache.ObjImageInterface
import ch.bailu.aat_lib.service.directory.DirectoryService
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface
import ch.bailu.aat_lib.service.elevation.ElevetionServiceInterface
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient
import ch.bailu.aat_lib.service.location.LocationService
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.render.RenderService
import ch.bailu.aat_lib.service.render.RenderServiceInterface
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.service.tracker.TrackerService
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface

class GtkServices (appContext: AppContext) : ServicesInterface {
    private val cacheService = CacheService(appContext)
    private val locationService: LocationService = LocationService(GtkSolidLocationProvider(appContext.storage), appContext.broadcaster)
    private val trackerService: TrackerService = TrackerService(
            SolidGtkDataDirectory(appContext.storage, appContext),
            GtkStatusIcon(),
            appContext.broadcaster,
            this
    )
    private val backgroundService = BackgroundService(appContext, appContext.broadcaster, 5)
    private val directoryService = DirectoryService(appContext)
    private val renderService = RenderService(appContext, appContext.mapDirectory)

    override fun getLocationService(): LocationServiceInterface {
        return locationService
    }

    override fun lock(simpleName: String) {}
    override fun free(simpleName: String) {}
    override fun lock(): Boolean {
        return true
    }

    override fun free() {}
    override fun getElevationService(): ElevetionServiceInterface {
        return ElevationService()
    }

    class ElevationService() : ElevetionServiceInterface {
        override fun getElevation(latitudeE6: Int, longitudeE6: Int): Short {
            return 0
        }

        override fun requestElevationUpdates(owner: ElevationUpdaterClient?, srtmTileCoordinates: Array<out Dem3Coordinates>?) {

        }

        override fun requestElevationUpdates() {

        }

        override fun cancelElevationUpdates(objGpxStatic: ElevationUpdaterClient?) {

        }

    }

    override fun getRenderService(): RenderServiceInterface {
        return renderService
    }

    override fun getSensorService(): SensorServiceInterface {
        return object : SensorServiceInterface {
            override fun getInformationOrNull(infoID: Int): GpxInformation? {
                return null
            }

            override fun getInformation(iid: Int): GpxInformation? {
                return null
            }

            override fun updateConnections() {}
            override fun scan() {}
        }
    }

    override fun getTrackerService(): TrackerServiceInterface {
        return trackerService
    }

    override fun getIconMapService(): IconMapServiceInterface {
        return object : IconMapServiceInterface {
            override fun getIconSVG(point: GpxPointInterface, icon_size: Int): ObjImageInterface? {
                return null
            }

            override fun toAssetPath(key: Int, value: String): String {
                return ""
            }

            override fun toAssetPath(gpxPointNode: GpxPointNode): String {
                return ""
            }
        }
    }

    override fun getBackgroundService(): BackgroundServiceInterface {
        return backgroundService
    }

    override fun getCacheService(): CacheServiceInterface {
        return cacheService
    }

    override fun getDirectoryService(): DirectoryServiceInterface {
       return directoryService
    }
}
