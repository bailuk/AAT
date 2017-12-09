package ch.bailu.aat.services.cache.elevation;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.OnObject;
import ch.bailu.aat.util.AppBroadcaster;

public class RasterInitializer extends ProcessHandle {

    private final String iid;
    private long size;


    public RasterInitializer(String i) {
        iid = i;
    }

    @Override
    public long bgOnProcess(final ServiceContext sc) {
        size = 0;

        new OnObject(sc, iid, ElevationTile.class) {
            @Override
            public void run(ObjectHandle obj) {
                ElevationTile owner = (ElevationTile) obj;
                size = owner.bgOnProcessInitializer(sc);

            }
        };
        return size;
    }
}



