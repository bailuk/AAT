package ch.bailu.aat.services.cache.elevation;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat.services.cache.OnObject;
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public final class SubTilePainter extends BackgroundTask {
    private final Dem3Tile tile;

    private final String iid;


    private final ServicesInterface scontext;

    public SubTilePainter(ServicesInterface sc, String i, Dem3Tile t) {
        scontext = sc;

        iid = i;
        tile = t;
    }


    @Override
    public void onInsert() {
        tile.lock(this);
    }


    @Override
    public long bgOnProcess(final AppContext appContext) {
        final long[] size = {0};

        new OnObject(appContext, iid, ObjTileElevation.class) {
            @Override
            public void run(Obj handle) {
                ObjTileElevation owner = (ObjTileElevation) handle;

                size[0] = owner.bgOnProcessPainter(tile);
                appContext.getBroadcaster().broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, iid);
            }
        };

        return size[0];
    }

    @Override
    public void onRemove() {
        tile.free(this);

        new InsideContext(scontext) {
            @Override
            public void run() {
                scontext.getElevationService().requestElevationUpdates();
            }
        };
    }
}

