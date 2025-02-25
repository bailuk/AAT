package ch.bailu.aat_gtk.service

import ch.bailu.aat_gtk.solid.GtkSolidLocationProvider
import ch.bailu.aat_gtk.view.GtkStatusIcon
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.service.IconMapServiceInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.BackgroundService
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface
import ch.bailu.aat_lib.service.cache.CacheService
import ch.bailu.aat_lib.service.cache.CacheServiceInterface
import ch.bailu.aat_lib.service.directory.DirectoryService
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface
import ch.bailu.aat_lib.service.elevation.ElevationService
import ch.bailu.aat_lib.service.elevation.ElevationServiceInterface
import ch.bailu.aat_lib.service.icons.IconMapService
import ch.bailu.aat_lib.service.location.LocationService
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.render.RenderService
import ch.bailu.aat_lib.service.render.RenderServiceInterface
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.service.tileremover.TileRemoverService
import ch.bailu.aat_lib.service.tracker.TrackerService
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface

class GtkServices (appContext: AppContext) : ServicesInterface {
    private val lazyLocationService by lazy { LocationService(
        GtkSolidLocationProvider(appContext.storage),
        appContext.broadcaster,
        getSensorService()
    ) }

    private val lazyTrackerService by lazy { TrackerService(
            appContext.dataDirectory,
            GtkStatusIcon(),
            appContext.broadcaster,
            this
    ) }

    private val lazyTileRemoverService by lazy {
        TileRemoverService(appContext)
    }

    private val lazyCacheService by lazy { CacheService(appContext) }
    private val lazyBackgroundService by lazy { BackgroundService(appContext, appContext.broadcaster, 5) }
    private val lazyDirectoryService by lazy { DirectoryService(appContext) }
    private val lazyRenderService by lazy { RenderService(appContext, appContext.mapDirectory) }

    private val lazyElevationService by lazy { ElevationService(appContext) }
    private val lazyIconMapService by lazy { IconMapService(appContext.services, appContext.assets) }

    override fun getLocationService(): LocationServiceInterface {
        return lazyLocationService
    }

    override fun lock(resource: String) {}
    override fun free(resource: String) {}
    override fun lock(): Boolean {
        return true
    }

    override fun free() {}
    override fun getElevationService(): ElevationServiceInterface {
        return lazyElevationService
    }

    override fun getRenderService(): RenderServiceInterface {
        return lazyRenderService
    }

    override fun getTileRemoverService(): TileRemoverService {
        return lazyTileRemoverService
    }

    override fun getSensorService(): SensorServiceInterface {
        return object : SensorServiceInterface {
            override fun getInformationOrNull(infoID: Int): GpxInformation? {
                return null
            }

            override fun getInfo(iid: Int): GpxInformation {
                return GpxInformation.NULL
            }

            override fun updateConnections() {}
            override fun scan() {}
        }
    }

    override fun getTrackerService(): TrackerServiceInterface {
        return lazyTrackerService
    }

    override fun getIconMapService(): IconMapServiceInterface {
        return lazyIconMapService
    }

    override fun getBackgroundService(): BackgroundServiceInterface {
        return lazyBackgroundService
    }

    override fun getCacheService(): CacheServiceInterface {
        return lazyCacheService
    }

    override fun getDirectoryService(): DirectoryServiceInterface {
       return lazyDirectoryService
    }
}
