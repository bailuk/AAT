package ch.bailu.aat.services.cache.elevation;

import android.content.Context;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;

public class RasterInitializer extends ProcessHandle {

    private final ServiceContext scontext;
    private final String iid;


    public RasterInitializer(ServiceContext sc, String i) {
        scontext = sc;
        iid = i;
    }

    @Override
    public long bgOnProcess() {
        long size = 0;

        if (scontext.lock()) {
            ObjectHandle handle = scontext.getCacheService().getObject(iid);

            if (handle instanceof ElevationTile) {
                ElevationTile owner = (ElevationTile) handle;
                size = owner.bgOnProcessInitializer();
            }

            handle.free();
            scontext.free();
        }
        return size;
    }


    @Override
    public void broadcast(Context context) {
        // FIXME only broadcast if there are subTiles to paint
        AppBroadcaster.broadcast(context, AppBroadcaster.REQUEST_ELEVATION_UPDATE, iid);
    }
}



