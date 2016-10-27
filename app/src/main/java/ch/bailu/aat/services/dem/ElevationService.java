package ch.bailu.aat.services.dem;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class ElevationService extends VirtualService {


    private final ElevationUpdater  elevationUpdater;

    public ElevationService(ServiceContext sc) {
        super(sc);
        elevationUpdater = new ElevationUpdater(sc);
    }

    public short getElevation(int laE6, int loE6) {
        return elevationUpdater.getElevation(laE6, loE6);
    }


    @Override
    public void close() {
        elevationUpdater.close();
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        // TODO Auto-generated method stub

    }


}
