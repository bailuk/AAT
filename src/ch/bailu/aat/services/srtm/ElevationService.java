package ch.bailu.aat.services.srtm;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;

public class ElevationService extends AbsService implements ElevationProvider {
    private static final Closeable NULL_CLOSEABLE= new Closeable() {
        @Override
        public void close() throws IOException {
            
        }
    };
    
    public static final Class<?> SERVICES[] = {
        CacheService.class,
        BackgroundService.class
    };

    private ElevationProvider elevation=ElevationProvider.NULL;
    private Closeable toClose=NULL_CLOSEABLE;

    @Override
    public void onCreate() {
        super.onCreate();

        connectToServices(SERVICES);
    }
    

    @Override
    public void onServicesUp() {
        try {
            ElevationUpdater e=new ElevationUpdater( this.getCacheService(), this.getBackgroundService());
            toClose=e;
            elevation=e;
            
        } catch (ServiceNotUpException e) {
            e.printStackTrace();
        }
    }
    
    
  
   

    
   
    
    @Override
    public void onDestroy() {
        try {
            toClose.close();
            toClose=NULL_CLOSEABLE;
            elevation=ElevationProvider.NULL;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public short getElevation(int laE6, int loE6) {
        return elevation.getElevation(laE6, loE6);
    }
}
