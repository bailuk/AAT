package ch.bailu.aat.services.dem;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class ElevationService extends VirtualService {


    private final Self self;

    public Self getSelf() {
        return self;
    }

    
    public ElevationService(ServiceContext sc) {
        super(sc);
        self = new SelfOn();
    }



    @Override
    public void close() {
        self.close();
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

        public SelfOn() {
            elevationUpdater = new ElevationUpdater(getSContext());
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

    @Override
    public void appendStatusText(StringBuilder builder) {
        // TODO Auto-generated method stub
        
    }


}
