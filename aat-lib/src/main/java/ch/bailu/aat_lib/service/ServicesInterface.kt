package ch.bailu.aat_lib.service

import ch.bailu.aat_lib.service.background.BackgroundServiceInterface
import ch.bailu.aat_lib.service.cache.CacheServiceInterface
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface
import ch.bailu.aat_lib.service.elevation.ElevationServiceInterface
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.render.RenderServiceInterface
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.service.tileremover.TileRemoverService
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface

interface ServicesInterface {
    fun getLocationService(): LocationServiceInterface
    fun getSensorService(): SensorServiceInterface
    fun getTrackerService(): TrackerServiceInterface
    fun getIconMapService(): IconMapServiceInterface
    fun getBackgroundService(): BackgroundServiceInterface
    fun getCacheService(): CacheServiceInterface
    fun getDirectoryService(): DirectoryServiceInterface
    fun getElevationService(): ElevationServiceInterface
    fun getRenderService(): RenderServiceInterface
    fun getTileRemoverService(): TileRemoverService

    fun lock(resource: String)
    fun free(resource: String)

    fun lock(): Boolean
    fun free()

    fun insideContext(runnable: Runnable) {
        if (lock()) {
            try {
                runnable.run()
            } finally {
                free()
            }
        }
    }
}
