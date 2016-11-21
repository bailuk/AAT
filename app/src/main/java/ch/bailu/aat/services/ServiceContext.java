package ch.bailu.aat.services;

import android.app.Notification;

import ch.bailu.aat.helpers.ContextWrapperInterface;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.location.LocationService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.services.tracker.TrackerService;

public interface ServiceContext extends ContextWrapperInterface {

    void lock(String s);
    void free(String s);

    boolean isUp();

    LocationService getLocationService();
    TrackerService getTrackerService();
    BackgroundService getBackgroundService();
    CacheService getCacheService();
    ElevationService getElevationService();
    IconMapService getIconMapService();
    DirectoryService getDirectoryService();
    TileRemoverService getTileRemoverService();

    void startForeground(int id, Notification notification);
    void stopForeground(boolean b);

    void appendStatusText(StringBuilder content);

}
