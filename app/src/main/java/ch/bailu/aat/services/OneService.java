package ch.bailu.aat.services;

import android.content.Context;

import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.services.tracker.TrackerService;

import static ch.bailu.aat.services.ServiceLink.ServiceNotUpError;

public class OneService extends AbsService  implements ServiceContext {

    private TrackerService tracker;
    private BackgroundService background;
    private IconMapService iconMap;
    private CacheService   cache;
    private DirectoryService directory;
    private ElevationService elevation;
    private TileRemoverService tileRemover;


    private boolean up = false;

    @Override 
    public void onCreate() {
        up = true;
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        if (tracker != null) {
            tracker.close();
            tracker = null;
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


        if (elevation != null) {
            elevation.close();
            elevation = null;
        }

        if (tileRemover != null) {
            tileRemover.close();
            tileRemover = null;
        }

        up = false;
        super.onDestroy();

    }


    @Override
    public void onLowMemory() {
        cache.onLowMemory();
        super.onLowMemory();
    }


    @Override
    public BackgroundService getBackgroundService() {
        if (isUp() && background == null) {
            background = new BackgroundService(this);
        }
        return background;
    }

    @Override
    public CacheService getCacheService() {
        if (isUp() && cache == null) {
            cache = new CacheService(this);
        }
        return cache;
    }

    @Override
    public ElevationService getElevationService() {
        if (isUp() && elevation == null)
            elevation = new ElevationService(this);
        return elevation;
    }

    @Override
    public IconMapService getIconMapService() {
        if (isUp() && iconMap == null)
            iconMap = new IconMapService(this);
        return iconMap;
    }

    @Override
    public DirectoryService getDirectoryService() {
        if (isUp() && directory == null)
            directory = new DirectoryService(this);
        return directory;
    }

    @Override
    public TrackerService getTrackerService() {
        if (isUp() && tracker == null)
            tracker = new TrackerService(this);
        return tracker;
    }

    @Override
    public TileRemoverService getTileRemoverService() {
        if (isUp() && tileRemover == null)
            tileRemover = new TileRemoverService(this);
        return tileRemover;
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        if (isUp()) {
            super.appendStatusText(builder);
            appendStatusText(tracker, builder);
            appendStatusText(background, builder);
            appendStatusText(cache, builder);
            appendStatusText(iconMap, builder);
            appendStatusText(directory, builder);
            appendStatusText(elevation, builder);
        }
    }

    public void appendStatusText(VirtualService service, StringBuilder builder) {
        if (isUp()) {
            builder.append("<h1>");
            builder.append(service.getClass().getSimpleName());
            builder.append("</h1>");

            builder.append("<p>");
            service.appendStatusText(builder);
            builder.append("</p>");
        }
    }

    @Override
    public Context getContext() {
        isUp(); return this;
    }

    public boolean isUp() {
        if (!up) {
            new ServiceNotUpError(OneService.class);
        }
        return up;
    }
}
