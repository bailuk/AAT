package ch.bailu.aat.services.srtm;

import java.io.Closeable;
import java.io.IOException;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;

public class ElevationService extends AbsService implements ElevationProvider {
    public static final Class<?> SERVICES[] = {
        CacheService.class,
        BackgroundService.class
    };
 
    private SrtmAccess srtmAccess=new SrtmAccess();
    private Closeable elevationUpdater = new Closeable() {
        @Override
        public void close() {}
        
    };

    @Override
    public void onCreate() {
        super.onCreate();

        connectToServices(SERVICES);
    }
    

    @Override
    public void onServicesUp() {
        try {
            elevationUpdater=new ElevationUpdater( this.getCacheService(), this.getBackgroundService());
            srtmAccess=new SRTMGL3GeneralAccess(this.getCacheService());
        } catch (ServiceNotUpException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        return srtmAccess.toString();
    }
    
    @Override
    public short getElevation(IGeoPoint p) {
        
        return srtmAccess.getElevation(p);
    }

    
    @Override
    public boolean isReady() {
        return srtmAccess.isReady();
    }

    
    @Override
    public void onDestroy() {
        try {
            srtmAccess.close();
            elevationUpdater.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Override
    public short getElevation(int laE6, int loE6) {
        return srtmAccess.getElevation(laE6, loE6);
    }


    @Override
    public short getElevation(int index) {
        return srtmAccess.getElevation(index);
    }

    

}
