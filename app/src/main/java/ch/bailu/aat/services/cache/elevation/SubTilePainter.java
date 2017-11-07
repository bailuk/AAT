package ch.bailu.aat.services.cache.elevation;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.util.AppBroadcaster;

public class SubTilePainter extends ProcessHandle {
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
    public long bgOnProcess(ServiceContext sc) {
        long size = 0;

        if (sc.lock()) {
            ObjectHandle handle = sc.getCacheService().getObject(iid);

            if (handle instanceof ElevationTile) {

                ElevationTile owner = (ElevationTile) handle;

                size = owner.bgOnProcessPainter(tile);
                AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE, iid);
            }

            handle.free();
            sc.free();
        }

        return size;
    }

    @Override
    public void onRemove() {
        tile.free(this);

        if (scontext.lock()) {
            scontext.getElevationService().requestElevationUpdates();
            scontext.free();
        }
    }
}

