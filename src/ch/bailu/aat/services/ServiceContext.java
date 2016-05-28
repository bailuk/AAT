package ch.bailu.aat.services;

import android.app.Service;
import ch.bailu.aat.helpers.ContextWrapperInterface;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.editor.EditorService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.tracker.TrackerService;

public abstract class ServiceContext implements ContextWrapperInterface {


    public static class ServiceNotUpException extends Exception {
        private static final long serialVersionUID = 5632759660184034845L;

        public ServiceNotUpException(Class<?> service)  {
            super("Service '" + Service.class.getSimpleName() + "' is not running.*");
        }
    }


    public abstract AbsService getService(Class<?> s) throws ServiceNotUpException;


    public BackgroundService.Self getBackgroundService() {
        BackgroundService.Self s;
        try {
            s=((BackgroundService) getService(BackgroundService.class)).getSelf();

        } catch (Exception e) {
            s= new BackgroundService.Self();

        }
        return s;
    }


    public CacheService.Self getCacheService()  {
        CacheService.Self s;
        try {
            s = ((CacheService)getService(CacheService.class)).getSelf();

        } catch (ServiceNotUpException e) {
            s = new CacheService.Self();

        }
        return s;
    }


    public ElevationService.Self getElevationService() {
        ElevationService.Self s;
        try {
            s = ((ElevationService)getService(ElevationService.class)).getSelf();

        } catch (ServiceNotUpException e) {
            s = new ElevationService.Self();

        }
        return s;
    }


    public IconMapService.Self getIconMapService() {
        IconMapService.Self s;
        try {
            s = ((IconMapService)getService(IconMapService.class)).getSelf();

        } catch (ServiceNotUpException e) {
            s = new IconMapService.Self();

        }
        return s;
    }
    
    
    public DirectoryService.Self getDirectoryService() {
        DirectoryService.Self s;
        try {
            s = ((DirectoryService)getService(DirectoryService.class)).getSelf();

        } catch (ServiceNotUpException e) {
            s = new DirectoryService.Self();

        }
        return s;
    }

    
    public EditorService.Self getEditorService()  {
        EditorService.Self s;
        try {
            s = ((EditorService)getService(EditorService.class)).getSelf();

        } catch (ServiceNotUpException e) {
            s = new EditorService.Self();

        }
        return s;
    }

    


    public TrackerService.Self getTrackerService() {
        TrackerService.Self s;
        try {
            s = ((TrackerService)getService(TrackerService.class)).getSelf();

        } catch (ServiceNotUpException e) {
            s = new TrackerService.Self();

        }
        return s;
    }




    public void appendStatusText(StringBuilder content) {
        for (Class<?> s: ServiceLink.ALL_SERVICES) {
            try {
                getService(s).appendStatusText(content);
            } catch (ServiceNotUpException e) {
                content.append("<p>ERROR*: ");
                content.append(e.getMessage());
                content.append("</p>");
            }
        }
    }
}
