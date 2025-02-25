package ch.bailu.aat_lib.mock

import ch.bailu.aat_lib.service.IconMapServiceInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface
import ch.bailu.aat_lib.service.cache.CacheServiceInterface
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface
import ch.bailu.aat_lib.service.elevation.ElevationServiceInterface
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.render.RenderServiceInterface
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface
import ch.bailu.aat_lib.service.tileremover.TileRemoverService
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface

class MockServices : ServicesInterface {
    override fun getLocationService(): LocationServiceInterface {
        TODO("Not yet implemented")
    }

    override fun getSensorService(): SensorServiceInterface {
        TODO("Not yet implemented")
    }

    override fun getTrackerService(): TrackerServiceInterface {
        TODO("Not yet implemented")
    }

    override fun getIconMapService(): IconMapServiceInterface {
        TODO("Not yet implemented")
    }

    override fun getBackgroundService(): BackgroundServiceInterface {
        TODO("Not yet implemented")
    }

    override fun getCacheService(): CacheServiceInterface {
        return MockCacheService()
    }

    override fun getDirectoryService(): DirectoryServiceInterface {
        TODO("Not yet implemented")
    }

    override fun getElevationService(): ElevationServiceInterface {
        TODO("Not yet implemented")
    }

    override fun getRenderService(): RenderServiceInterface {
        TODO("Not yet implemented")
    }

    override fun getTileRemoverService(): TileRemoverService {
        TODO("Not yet implemented")
    }

    override fun lock(resource: String) {
        TODO("Not yet implemented")
    }

    override fun lock(): Boolean {
        return true
    }

    override fun free(resource: String) {}
    override fun free() {}
}
