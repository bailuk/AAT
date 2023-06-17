package ch.bailu.aat.services

import android.content.Context
import ch.bailu.aat.app.AndroidAppContext
import ch.bailu.aat.preferences.location.AndroidSolidLocationProvider
import ch.bailu.aat.services.sensor.SensorService
import ch.bailu.aat.services.tileremover.TileRemoverService
import ch.bailu.aat.services.tracker.StatusIcon
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.map.SolidRendererThreads
import ch.bailu.aat_lib.service.IconMapServiceInterface
import ch.bailu.aat_lib.service.background.BackgroundService
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface
import ch.bailu.aat_lib.service.cache.CacheService
import ch.bailu.aat_lib.service.cache.CacheServiceInterface
import ch.bailu.aat_lib.service.directory.DirectoryService
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface
import ch.bailu.aat_lib.service.elevation.ElevationService
import ch.bailu.aat_lib.service.icons.IconMapService
import ch.bailu.aat_lib.service.location.LocationService
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.render.RenderService
import ch.bailu.aat_lib.service.render.RenderServiceInterface
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.service.tracker.TrackerService
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface
import ch.bailu.aat_lib.util.WithStatusText

class OneService : AbsService(), ServiceContext {
    private var location: LocationService? = null
    private var tracker: TrackerService? = null
    private var background: BackgroundServiceInterface? = null
    private var iconMap: IconMapService? = null
    private var cache: CacheServiceInterface? = null
    private var directory: DirectoryService? = null
    private var elevation: ElevationService? = null
    private var tileRemover: TileRemoverService? = null
    private var render: RenderService? = null
    private var ble: SensorService? = null

    private val appContext: AppContext by lazy {
        AndroidAppContext(this, this)
    }

    @Synchronized
    override fun onDestroy() {
        onDestroyCalled()

        tracker?.close()
        tracker = null

        location?.close()
        location = null

        background?.close()
        background = null

        iconMap?.close()
        iconMap = null

        cache?.close()
        cache = null

        directory?.close()
        directory = null

        elevation?.close()
        elevation = null

        tileRemover?.close()
        tileRemover = null

        render?.close()
        render = null

        ble?.close()
        ble = null
        super.onDestroy()
    }

    @Synchronized
    override fun onLowMemory() {
        cache?.onLowMemory()
        super.onLowMemory()
    }

    @Synchronized
    override fun getLocationService(): LocationServiceInterface {
        if (location == null) {
            location = LocationService(
                AndroidSolidLocationProvider(this),
                appContext.broadcaster,
                sensorService
            )
        }
        return location!!
    }

    @Synchronized
    override fun getBackgroundService(): BackgroundServiceInterface {
        if (background == null) {
            background = BackgroundService(
                appContext,
                appContext.broadcaster,
                SolidRendererThreads.numberOfBackgroundThreats()
            )
        }
        return background!!
    }

    @Synchronized
    override fun getCacheService(): CacheServiceInterface {
        if (cache == null) {
            cache = CacheService(appContext)
            elevationService
        }
        return cache!!
    }

    @Synchronized
    override fun getRenderService(): RenderServiceInterface {
        if (render == null) {
            render = RenderService(appContext, appContext.mapDirectory)
        }
        return render!!
    }

    @Synchronized
    override fun getSensorService(): SensorServiceInterface {
        if (ble == null) {
            ble = SensorService(this)
        }
        return ble!!
    }

    @Synchronized
    override fun getElevationService(): ElevationService {
        if (elevation == null) elevation = ElevationService(appContext)
        return elevation!!
    }

    @Synchronized
    override fun getIconMapService(): IconMapServiceInterface {
        if (iconMap == null) iconMap = IconMapService(this, appContext.assets)
        return iconMap!!
    }

    @Synchronized
    override fun getDirectoryService(): DirectoryServiceInterface {
        if (directory == null) directory = DirectoryService(appContext)
        return directory!!
    }

    @Synchronized
    override fun getTrackerService(): TrackerServiceInterface {
        if (tracker == null) tracker = TrackerService(appContext.dataDirectory, StatusIcon(this), appContext.broadcaster,this)
        return tracker!!
    }

    @Synchronized
    override fun getTileRemoverService(): TileRemoverService {
        if (tileRemover == null) tileRemover = TileRemoverService(appContext)
        return tileRemover!!
    }

    @Synchronized
    override fun appendStatusText(builder: StringBuilder) {
        super.appendStatusText(builder)
        appendStatusText(location, builder)
        appendStatusText(tracker, builder)
        appendStatusText(background, builder)
        appendStatusText(cache, builder)
        appendStatusText(iconMap, builder)
        appendStatusText(directory, builder)
    }

    @Synchronized
    private fun appendStatusText(service: WithStatusText?, builder: StringBuilder) {
        if (service != null) {
            builder.append("<h1>")
            builder.append(service.javaClass.simpleName)
            builder.append("</h1>")
            builder.append("<p>")
            service.appendStatusText(builder)
            builder.append("</p>")
        }
    }

    @Synchronized
    override fun getContext(): Context {
        return this
    }
}
