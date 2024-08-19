package ch.bailu.aat_lib.service;

import ch.bailu.aat_lib.service.background.BackgroundServiceInterface;
import ch.bailu.aat_lib.service.cache.CacheServiceInterface;
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface;
import ch.bailu.aat_lib.service.elevation.ElevationServiceInterface;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.render.RenderServiceInterface;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface;

public interface ServicesInterface {
    LocationServiceInterface getLocationService();
    SensorServiceInterface getSensorService();
    TrackerServiceInterface getTrackerService();
    IconMapServiceInterface getIconMapService();
    BackgroundServiceInterface getBackgroundService();
    CacheServiceInterface getCacheService();
    DirectoryServiceInterface getDirectoryService();
    ElevationServiceInterface getElevationService();
    RenderServiceInterface getRenderService();

    void lock(String simpleName);
    void free(String simpleName);

    boolean lock();
    void free();

    default void insideContext(Runnable runnable) {
        if (lock()) {
            try {
                runnable.run();
            }
            finally {
                free();
            }
        }
    }
}
