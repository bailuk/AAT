package ch.bailu.aat.services.overlay;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.ServiceContext.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;


public class OverlayService extends AbsService implements GpxInformation.ID {
    public static final int MAX_OVERLAYS=SolidOverlayFileList.MAX_OVERLAYS;
    
    public static final Class<?> SERVICES[] = {
        CacheService.class,
        BackgroundService.class,
    };
    
    
    private class SelfOff implements Closeable {
        public GpxInformation getInformation(int id) {
            return GpxInformation.NULL;
        }
        
        @Override
        public void close() {}
        
    }
    
    private class SelfOn extends SelfOff {
        
        private OverlayInformation[] overlayList = new OverlayInformation[MAX_OVERLAYS];

        public SelfOn() throws SecurityException, IOException, ServiceNotUpException {
            fillOverlayList();
        }
        
        private void fillOverlayList() throws SecurityException, IOException, ServiceNotUpException {
            for (int i=0; i<MAX_OVERLAYS; i++)
                overlayList[i]= new OverlayInformation(INFO_ID_OVERLAY+i, getServiceContext());
        }

        @Override
        public GpxInformation getInformation(int id) {
            id -= INFO_ID_OVERLAY;
            id = Math.min(MAX_OVERLAYS, id);
            id = Math.max(0, id);

            return overlayList[id];
        }
        

        
        @Override
        public void close() {
            for (int i=0; i<overlayList.length; i++) 
                overlayList[i].close();
        }
    }
    
    private SelfOff self = new SelfOff();
    
    @Override
    public void onCreate() {
        super.onCreate();

        connectToServices(SERVICES);
    }


    
    @Override
    public void onServicesUp() {
        try {

            self = new SelfOn();

        } catch (Exception e) {
            AppLog.e(OverlayService.this, e);
        }
    }

    


    public GpxInformation getInformation(int id) {
        return self.getInformation(id);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        self.close();
    }
}
