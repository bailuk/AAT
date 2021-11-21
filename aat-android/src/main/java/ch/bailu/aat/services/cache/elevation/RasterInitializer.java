package ch.bailu.aat.services.cache.elevation;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.OnObject;

public final class RasterInitializer extends BackgroundTask {

    private final String iid;
    private long size;


    public RasterInitializer(String i) {
        iid = i;
    }

    @Override
    public long bgOnProcess(final AppContext sc) {
        size = 0;

        new OnObject(sc, iid, ObjTileElevation.class) {
            @Override
            public void run(Obj obj) {
                ObjTileElevation owner = (ObjTileElevation) obj;
                size = owner.bgOnProcessInitializer(sc);

            }
        };
        return size;
    }
}



