package ch.bailu.aat.services.cache.elevation;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.util.AppBroadcaster;

public class SubTilePainter extends ProcessHandle {
    private final Dem3Tile tile;
    private final SubTile subTile;
    private final String iid;



    public SubTilePainter(String i, SubTile s, Dem3Tile t) {
        subTile = s;
        iid = i;
        tile = t;
    }


    @Override
    public String toString() {
        return subTile.coordinates.toString();
    }


    @Override
    public int hashCode() {
        return toString().hashCode();
    }


    @Override
    public long bgOnProcess(ServiceContext sc) {
        long size = 0;

        if (sc.lock()) {
            ObjectHandle handle = sc.getCacheService().getObject(iid);

            if (handle instanceof ElevationTile) {

                ElevationTile owner = (ElevationTile) handle;

                size = owner.bgOnProcessPainter(subTile, tile);
                AppBroadcaster.broadcast(sc.getContext(), AppBroadcaster.FILE_CHANGED_INCACHE, iid);
            }

            handle.free();
            sc.free();
        }

        return size;
    }

    @Override
    public void onInsert() {
        tile.lock();
    }


    @Override
    public void onRemove() {
        tile.free();
    }
}

