package ch.bailu.aat.services.dem;

import java.io.Closeable;

import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;

public class ElevationService extends AbsService {

    public static final Class<?> SERVICES[] = {
        CacheService.class,
        BackgroundService.class
    };

    private Self self = new Self();

    public Self getSelf() {
        return self;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        connectToServices(SERVICES);
    }


    @Override
    public void onServicesUp() {
        self.close();
        self = new SelfOn(getServiceContext());
    }




    @Override
    public void onDestroy() {
        self.close();
        self = new Self();
        super.onDestroy();
    }



    public static class Self implements Closeable, ElevationProvider {
        public short getElevation(int laE6, int loE6) {
            return 0;
        }

        @Override
        public void close() {
        }
    }

    public class SelfOn extends Self {
        private final ElevationUpdater  elevationUpdater;

        public SelfOn(ServiceContext sc) {
            elevationUpdater = new ElevationUpdater(sc);
        }
        @Override
        public short getElevation(int laE6, int loE6) {
            return elevationUpdater.getElevation(laE6, loE6);
        }


        @Override
        public void close() {
            elevationUpdater.close();
        }
    }


}
