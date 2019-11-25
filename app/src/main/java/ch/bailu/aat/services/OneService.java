package ch.bailu.aat.services;

import android.content.Context;

import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.sensor.SensorService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.location.LocationService;
import ch.bailu.aat.services.render.RenderService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.services.tracker.TrackerService;
import ch.bailu.aat.util.WithStatusText;

public final class OneService extends AbsService  implements ServiceContext {

    private LocationService location;
    private TrackerService tracker;
    private BackgroundService background;
    private IconMapService iconMap;
    private CacheService   cache;
    private DirectoryService directory;
    private ElevationService elevation;
    private TileRemoverService tileRemover;
    private RenderService render;
    private SensorService ble;



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
    public  synchronized LocationService getLocationService() {
        if (location == null) {
            location = new LocationService(this);
        }
        return location;
    }

    @Override
    public  synchronized BackgroundService getBackgroundService() {
        if (background == null) {
            background = new BackgroundService(this);
        }
        return background;
    }

    @Override
    public synchronized CacheService getCacheService() {
        if (cache == null) {
            cache = new CacheService(this);
            getElevationService();
        }
        return cache;
    }

    @Override
    public synchronized  RenderService getRenderService() {
        if (render == null) {
            render = new RenderService(this);
        }
        return render;
    }

    @Override
    public SensorService getSensorService() {
        if (ble == null) {
            ble = new SensorService(this);
        }
        return ble;
    }

    @Override
    public synchronized  ElevationService getElevationService() {
        if (elevation == null)
            elevation = new ElevationService(this);
        return elevation;
    }

    @Override
    public  synchronized IconMapService getIconMapService() {
        if (iconMap == null)
            iconMap = new IconMapService(this);
        return iconMap;
    }

    @Override
    public synchronized  DirectoryService getDirectoryService() {
        if (directory == null)
            directory = new DirectoryService(this);
        return directory;
    }

    @Override
    public synchronized  TrackerService getTrackerService() {
        if (tracker == null)
            tracker = new TrackerService(this);
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
