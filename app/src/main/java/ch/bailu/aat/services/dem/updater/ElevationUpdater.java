package ch.bailu.aat.services.dem.updater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Iterator;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.dem.loader.Dem3Loader;
import ch.bailu.aat.services.dem.loader.Dem3Tiles;
import ch.bailu.aat.services.dem.tile.Dem3Status;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.services.dem.tile.ElevationProvider;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppLog;

public class ElevationUpdater implements Closeable {


    private final PendingUpdatesMap pendingUpdates = new PendingUpdatesMap();

    private final ServiceContext scontext;
    private final Context context;

    private final Dem3Loader loader;
    private final Dem3Tiles tiles;

    public ElevationUpdater(ServiceContext cs, Dem3Loader d, Dem3Tiles t) {
        scontext = cs;
        context = cs.getContext();
        tiles = t;
        loader = d;

        AppBroadcaster.register(context, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
    }


    private final BroadcastReceiver onFileChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppIntent.getFile(intent);

            synchronized(ElevationUpdater.this) {
                if (tiles.have(id)) {
                    requestElevationUpdates();
                }
            }
        }
    };





    public synchronized void requestElevationUpdates(ElevationUpdaterClient e,
                                                     SrtmCoordinates[] coordinates) {
        for (SrtmCoordinates c : coordinates) {
            addObject(c, e);
        }

        requestElevationUpdates();
    }

    public  synchronized void requestElevationUpdates() {
        updateClients();
        loadTiles();
    }

    public  synchronized void cancelElevationUpdates(ElevationUpdaterClient e) {
        pendingUpdates.remove(e);
    }


    private void addObject(SrtmCoordinates c, ElevationUpdaterClient e) {
        pendingUpdates.add(c, e);
    }


    private void loadTiles() {
        Iterator<SrtmCoordinates> c = pendingUpdates.coordinates();

        while(c.hasNext()) {
            Dem3Tile tile = loader.requestDem3Tile(c.next());

            if (tile == null) return;
        }
    }



    private void updateClients() {
        int t=0;
        Dem3Tile tile;
        while ((tile=tiles.get(t)) != null) {
            updateClients(tile);
            t++;
        }
    }


    private void updateClients(Dem3Tile tile) {

        tile.lock(this);

        if (tile.getStatus() == Dem3Status.VALID) {
            ArrayList<ElevationUpdaterClient> l = pendingUpdates.get(tile.getCoordinates());

            int size =0 ;

            if (l != null) {
                size = l.size();

                for (ElevationUpdaterClient e : l) {

                    e.updateFromSrtmTile(scontext, tile);
                }
            }
            AppLog.d(this, "update " + tile.getCoordinates().toString() + "["+size + "]");
        }

        if (tile.getStatus() == Dem3Status.EMPTY) {
            AppLog.d(this, "update " + tile.getCoordinates().toString() + "[E!]");
        }

        if (tile.getStatus() == Dem3Status.VALID || tile.getStatus() == Dem3Status.EMPTY) {
            pendingUpdates.remove(tile.getCoordinates());
        }

        tile.free(this);


    }


    @Override
    public  void close() {
        scontext.getContext().unregisterReceiver(onFileChanged);
    }

}
