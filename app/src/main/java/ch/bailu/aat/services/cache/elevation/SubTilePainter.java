package ch.bailu.aat.services.cache.elevation;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.BackgroundTask;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.OnObject;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.util.AppBroadcaster;

public class SubTilePainter extends BackgroundTask {
    private final Dem3Tile tile;

    private final String iid;


    private final ServiceContext scontext;

    public SubTilePainter(ServiceContext sc, String i, Dem3Tile t) {
        scontext = sc;

        iid = i;
        tile = t;
    }


    @Override
    public void onInsert() {
        tile.lock(this);
    }


    @Override
    public long bgOnProcess(final ServiceContext sc) {
        final long[] size = {0};

        new OnObject(sc, iid, ElevationTile.class) {
            @Override
            public void run(ObjectHandle handle) {
                ElevationTile owner = (ElevationTile) handle;

                size[0] = owner.bgOnProcessPainter(tile);
                AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE, iid);
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

