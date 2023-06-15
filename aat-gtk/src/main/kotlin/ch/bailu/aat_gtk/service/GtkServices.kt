package ch.bailu.aat_gtk.service

import ch.bailu.aat_gtk.solid.GtkSolidLocationProvider
import ch.bailu.aat_gtk.solid.SolidGtkDataDirectory
import ch.bailu.aat_gtk.view.GtkStatusIcon
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxInformation
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
import ch.bailu.aat_lib.service.tracker.TrackerService
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface

class GtkServices (appContext: AppContext) : ServicesInterface {
    private val locationService by lazy { LocationService(
        GtkSolidLocationProvider(appContext.storage),
        appContext.broadcaster,
        sensorService
    ) }

    private val trackerService by lazy { TrackerService(
            SolidGtkDataDirectory(appContext.storage, appContext),
            GtkStatusIcon(),
            appContext.broadcaster,
            this
    ) }

    private val cacheService by lazy { CacheService(appContext) }
    private val backgroundService by lazy { BackgroundService(appContext, appContext.broadcaster, 5) }
    private val directoryService by lazy { DirectoryService(appContext) }
    private val renderService by lazy { RenderService(appContext, appContext.mapDirectory) }

    private val elevationService by lazy { ElevationService(appContext) }
    private val iconMapService by lazy { IconMapService(appContext.services, appContext.assets) }

    override fun getLocationService(): LocationServiceInterface {
        return locationService
    }

    override fun lock(simpleName: String) {}
    override fun free(simpleName: String) {}
    override fun lock(): Boolean {
        return true
    }

    override fun free() {}
    override fun getElevationService(): ElevationServiceInterface {
        return elevationService
    }

    override fun getRenderService(): RenderServiceInterface {
        return renderService
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
        return trackerService
    }

    override fun getIconMapService(): IconMapServiceInterface {
        return iconMapService
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
