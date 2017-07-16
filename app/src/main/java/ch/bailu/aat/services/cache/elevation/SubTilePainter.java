package ch.bailu.aat.services.cache.elevation;

import android.content.Context;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.util.AppBroadcaster;

public class SubTilePainter extends ProcessHandle {
    private final Dem3Tile tile;
    private final SubTile subTile;

    private final ServiceContext scontext;
    private final String iid;



    public SubTilePainter(ServiceContext sc, String i, SubTile s, Dem3Tile t) {
        subTile = s;
        scontext = sc;
        iid = i;
        tile = t;
        tile.lock();
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
    public long bgOnProcess() {
        long size = 0;

        if (scontext.lock()) {
            ObjectHandle handle = scontext.getCacheService().getObject(iid);

            if (handle instanceof ElevationTile) {

                ElevationTile owner = (ElevationTile) handle;

                size = owner.bgOnProcessPainter(subTile, tile);
            }

            handle.free();
            scontext.free();
        }

        tile.free(); // FIXME: does not allways get called
        return size;
    }


    @Override
    public void broadcast(Context context) {
        AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, iid);
    }
}

