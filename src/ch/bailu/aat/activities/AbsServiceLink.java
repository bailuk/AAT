package ch.bailu.aat.activities;


import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.MultiServiceLink;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.directory.DirectoryService;
import ch.bailu.aat.services.editor.EditorService;
import ch.bailu.aat.services.overlay.OverlayService;
import ch.bailu.aat.services.srtm.ElevationService;
import ch.bailu.aat.services.tracker.TrackerService;

public abstract class AbsServiceLink extends AbsActivity implements GpxInformation.ID{


    private MultiServiceLink serviceLink=MultiServiceLink.NULL_SERVICE_LINK;

    public void connectToServices(Class<?>[] services) {
        serviceLink=new MultiServiceLink(this,services) {

            @Override
            public void onServicesUp() {
                AbsServiceLink.this.onServicesUp();
            }
        };
    }

    public abstract void onServicesUp();


    @Override
    public void onDestroy() {
        serviceLink.close();
        serviceLink=null;
        super.onDestroy();
    }    


    public AbsService getService(Class<?> s) throws ServiceNotUpException {
        return serviceLink.getService(s);
    }
    
    
    public OverlayService getOverlayService() throws ServiceNotUpException {
        return (OverlayService) getService(OverlayService.class);
    }
    
    
    public EditorService getEditorService() throws ServiceNotUpException {
        return (EditorService) getService(EditorService.class);
    }

    
    public CacheService getCacheService() throws ServiceNotUpException {
        return (CacheService) getService(CacheService.class);
    }

    public BackgroundService getBackgroundService() throws ServiceNotUpException {
        return (BackgroundService) getService(BackgroundService.class);
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
    
    public boolean isTrackerServiceConnected() {
        return serviceLink.isServiceUp(TrackerService.class);
    }


    


    public void onStartPauseClick() throws ServiceNotUpException {
        getTrackerService().getState().onStartPauseResume();
    }
}
