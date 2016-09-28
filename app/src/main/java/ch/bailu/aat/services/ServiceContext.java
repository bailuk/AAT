package ch.bailu.aat.services;

import android.app.Service;
import ch.bailu.aat.helpers.ContextWrapperInterface;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.services.tracker.TrackerService;

public abstract class ServiceContext implements ContextWrapperInterface {


    public static class ServiceNotUpException extends Exception {
        private static final long serialVersionUID = 5632759660184034845L;

        public ServiceNotUpException(Class<?> service)  {
            super("Service '" + Service.class.getSimpleName() + "' is not running.*");
        }
    }


    public abstract OneService getService() throws ServiceNotUpException;

    public abstract void lock(String s);
    public abstract void free(String s);

    public BackgroundService.Self getBackgroundService() {
        BackgroundService s=null;
        try {
            s=getService().background;

        } catch (Exception e) {
            s=null;

        }

        if (s==null) return new BackgroundService.Self();
        return s.getSelf();
    }


    public CacheService.Self getCacheService()  {
        CacheService s=null;
        try {
            s=getService().cache;

        } catch (Exception e) {
            s=null;

        }

        if (s==null) return new CacheService.Self();
        return s.getSelf(); 
    }


    public ElevationService.Self getElevationService() {
        ElevationService s=null;
        try {
            s=getService().elevation;

        } catch (Exception e) {
            s=null;

        }

        if (s==null) return new ElevationService.Self();
        return s.getSelf();    }


    public IconMapService.Self getIconMapService() {
        IconMapService s=null;
        try {
            s=getService().iconMap;

        } catch (Exception e) {
            s=null;

        }

        if (s==null) return new IconMapService.Self();
        return s.getSelf();    }


    public DirectoryService.Self getDirectoryService() {
        DirectoryService s=null;
        try {
            s=getService().directory;

        } catch (Exception e) {
            s=null;

        }

        if (s==null) return new DirectoryService.Self();
        return s.getSelf();    }



    public TrackerService.Self getTrackerService() {
        TrackerService s=null;
        try {
            s=getService().tracker;

        } catch (Exception e) {
            s=null;

        }

        if (s==null) return new TrackerService.Self();
        return s.getSelf();
    }


    public TileRemoverService getTileRemoverService() {
        try {
            return getService().tileRemover;

        } catch (Exception e) {
            return null;

        }
    }


    public void appendStatusText(StringBuilder content) {
        try {
            getService().appendStatusText(content);
        } catch (ServiceNotUpException e) {
            content.append("<p>ERROR*: ");
            content.append(e.getMessage());
            content.append("</p>");
        }
    }
    
}
