package ch.bailu.aat.services;

import android.app.Service;
import ch.bailu.aat.helpers.ContextWrapperInterface;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.dem.ElevationService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.editor.EditorService;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.services.overlay.OverlayService;
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
            s= BackgroundService.NULL_SELF;
            
        }
        return s;
    }


    public OverlayService getOverlayService() throws ServiceNotUpException {
        return (OverlayService) getService(OverlayService.class);
    }


    public EditorService getEditorService() throws ServiceNotUpException {
        return (EditorService) getService(EditorService.class);
    }


    public CacheService.Self getCacheService()  {
        CacheService.Self s;
        try {
            s = ((CacheService)getService(CacheService.class)).getSelf();

        } catch (ServiceNotUpException e) {
            s = CacheService.NULL_SELF;

        }
        return s;

    }


    public TrackerService getTrackerService() throws ServiceNotUpException {
        return (TrackerService) getService(TrackerService.class);
    }

    public DirectoryService getDirectoryService() throws ServiceNotUpException {
        return (DirectoryService)getService(DirectoryService.class);
    }


    public ElevationService getElevationService() throws ServiceNotUpException {
        return (ElevationService)getService(ElevationService.class);
    }

    public IconMapService getIconMapService() throws ServiceNotUpException {
        return (IconMapService)getService(IconMapService.class);
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
