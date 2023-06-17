package ch.bailu.aat.services;

import android.content.Context;

import ch.bailu.aat.app.AndroidAppContext;
import ch.bailu.aat.preferences.location.AndroidSolidLocationProvider;
import ch.bailu.aat.services.sensor.SensorService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.services.tracker.StatusIcon;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.preferences.map.SolidRendererThreads;
import ch.bailu.aat_lib.service.IconMapServiceInterface;
import ch.bailu.aat_lib.service.background.BackgroundService;
import ch.bailu.aat_lib.service.background.BackgroundServiceInterface;
import ch.bailu.aat_lib.service.cache.CacheService;
import ch.bailu.aat_lib.service.cache.CacheServiceInterface;
import ch.bailu.aat_lib.service.directory.DirectoryService;
import ch.bailu.aat_lib.service.directory.DirectoryServiceInterface;
import ch.bailu.aat_lib.service.elevation.ElevationService;
import ch.bailu.aat_lib.service.icons.IconMapService;
import ch.bailu.aat_lib.service.location.LocationService;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.render.RenderService;
import ch.bailu.aat_lib.service.render.RenderServiceInterface;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;
import ch.bailu.aat_lib.service.tracker.TrackerService;
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface;
import ch.bailu.aat_lib.util.WithStatusText;

public final class OneService extends AbsService  implements ServiceContext {

    private LocationService location;
    private TrackerService tracker;
    private BackgroundServiceInterface background;
    private IconMapService iconMap;
    private CacheServiceInterface cache;
    private DirectoryService directory;
    private ElevationService elevation;
    private TileRemoverService tileRemover;
    private RenderService render;
    private SensorService ble;
    private AppContext appContext;

    @Override
    public  void onCreate() {
        super.onCreate();
    }

    @Override
    public  synchronized void onDestroy() {

        onDestroyCalled();

        if (tracker != null) {
            tracker.close();
            tracker = null;
        }

        if (location != null) {
            location.close();
            location = null;
        }

        if (background != null) {
            background.close();
            background = null;
        }

        if (iconMap != null) {
            iconMap.close();
            iconMap = null;
        }

        if (cache != null) {
            cache.close();
            cache = null;
        }

        if (directory != null) {
            directory.close();
            directory = null;
        }

        if (elevation != null) {
            elevation.close();
            elevation = null;
        }

        if (tileRemover != null) {
            tileRemover.close();
            tileRemover = null;
        }

        if (render != null) {
            render.close();
            render = null;
        }

        if (ble != null) {
            ble.close();
            ble = null;
        }

        super.onDestroy();
    }


    @Override
    public  synchronized void onLowMemory() {
        if  (cache != null)
            cache.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public  synchronized LocationServiceInterface getLocationService() {
        if (location == null) {
            location = new LocationService(new AndroidSolidLocationProvider(this), getAppContext().getBroadcaster(), getSensorService());
        }
        return location;
    }

    @Override
    public  synchronized BackgroundServiceInterface getBackgroundService() {
        if (background == null) {
            background = new BackgroundService(getAppContext(), getAppContext().getBroadcaster(), SolidRendererThreads.numberOfBackgroundThreats());
        }
        return background;
    }

    @Override
    public synchronized CacheServiceInterface getCacheService() {
        if (cache == null) {
            cache = new CacheService(getAppContext());
            getElevationService();
        }
        return cache;
    }

    @Override
    public synchronized RenderServiceInterface getRenderService() {
        if (render == null) {
            render = new RenderService(getAppContext(),getAppContext().getMapDirectory());
        }
        return render;
    }

    @Override
    public SensorServiceInterface getSensorService() {
        if (ble == null) {
            ble = new SensorService(this);
        }
        return ble;
    }

    @Override
    public synchronized  ElevationService getElevationService() {
        if (elevation == null)
            elevation = new ElevationService(getAppContext());
        return elevation;
    }

    private AppContext getAppContext() {
        if (appContext == null) {
            appContext = new AndroidAppContext(this, this);
        }
        return appContext;
    }

    @Override
    public  synchronized IconMapServiceInterface getIconMapService() {
        if (iconMap == null)
            iconMap = new IconMapService(this, getAppContext().getAssets());
        return iconMap;
    }

    @Override
    public synchronized DirectoryServiceInterface getDirectoryService() {
        if (directory == null)
            directory = new DirectoryService(getAppContext());
        return directory;
    }

    @Override
    public synchronized TrackerServiceInterface getTrackerService() {
        if (tracker == null)
            tracker = new TrackerService(
                    getAppContext().getDataDirectory(),
                    new StatusIcon(this),
                    getAppContext().getBroadcaster(),
                    this);
        return tracker;
    }

    @Override
    public synchronized  TileRemoverService getTileRemoverService() {
        if (tileRemover == null)
            tileRemover = new TileRemoverService(this);
        return tileRemover;
    }

    @Override
    public synchronized  void appendStatusText(StringBuilder builder) {
            super.appendStatusText(builder);
            appendStatusText(location, builder);
            appendStatusText(tracker, builder);
            appendStatusText(background, builder);
            appendStatusText(cache, builder);
            appendStatusText(iconMap, builder);
            appendStatusText(directory, builder);
    }

    public synchronized  void appendStatusText(WithStatusText service, StringBuilder builder) {
        if (service != null) {

            builder.append("<h1>");
            builder.append(service.getClass().getSimpleName());
            builder.append("</h1>");

            builder.append("<p>");
            service.appendStatusText(builder);
            builder.append("</p>");
        }
    }

    @Override
    public  synchronized Context getContext() {
        return this;
    }
}
