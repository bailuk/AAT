package ch.bailu.aat.services.cache.elevation;

import android.content.Context;

import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.services.cache.OnObject;
import ch.bailu.aat.services.elevation.tile.Dem3Tile;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public final class SubTilePainter extends BackgroundTask {
    private final Dem3Tile tile;

    private final String iid;


    private final ServiceContext scontext;

    public SubTilePainter(ServiceContext sc, String i, Dem3Tile t) {
        scontext = sc;

        iid = i;
        tile = t;
    }


    @Override
    public void onInsert(Context c) {
        tile.lock(this);
    }


    @Override
    public long bgOnProcess(final ServiceContext sc) {
        final long[] size = {0};

        new OnObject(sc, iid, ObjTileElevation.class) {
            @Override
            public void run(Obj handle) {
                ObjTileElevation owner = (ObjTileElevation) handle;

                size[0] = owner.bgOnProcessPainter(tile);
                OldAppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE, iid);
            }
        };

        return size[0];
    }

    @Override
    public void onRemove(Context c) {
        tile.free(this);

        new InsideContext(scontext) {
            @Override
            public void run() {
                scontext.getElevationService().requestElevationUpdates();
            }
        };
    }
}

