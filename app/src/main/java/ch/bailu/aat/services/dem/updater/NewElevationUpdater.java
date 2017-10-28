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
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;

public class NewElevationUpdater implements Closeable {


    private final PendingUpdatesMap pendingUpdates = new PendingUpdatesMap();

    private final ServiceContext scontext;
    private final Context context;

    private final Dem3Loader loader;
    private final Dem3Tiles tiles;

    public NewElevationUpdater(ServiceContext cs, Dem3Loader d, Dem3Tiles t) {
        scontext = cs;
        context = cs.getContext();
        tiles = t;
        loader = d;

        AppBroadcaster.register(context, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
    }


    public void requestElevationUpdates(ElevationUpdaterClient e) {
        SrtmCoordinates[] coordinates = e.getSrtmTileCoordinates();

        for (SrtmCoordinates c : coordinates) {
            addObject(c, e);
        }

        updateObjects();
        loadTiles();
    }



    public void cancelElevationUpdates(ElevationUpdaterClient e) {
        pendingUpdates.remove(e);
    }


    private final BroadcastReceiver onFileChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppIntent.getFile(intent);

            if (tiles.have(id)){
                updateObjects();
            }
            loadTiles();
        }
    };


    private void addObject(SrtmCoordinates c, ElevationUpdaterClient e) {
        pendingUpdates.add(c, e);
    }


    private void loadTiles() {
        Iterator<SrtmCoordinates> c = pendingUpdates.coordinates();

        while(c.hasNext()) {
            if (loader.requestDem3Tile(c.next()) == null) return;
        }
    }



    private void updateObjects() {
        int t=0;
        Dem3Tile tile;
        while ((tile=tiles.get(t)) != null) {
            updateObjects(tile);
            t++;
        }
    }


    private void updateObjects(Dem3Tile tile) {
        if (tile.isLoaded()) {
            ArrayList<ElevationUpdaterClient> l = pendingUpdates.get(tile.getCoordinates());

            if (l != null) {
                for (ElevationUpdaterClient e : l) {
                    e.updateFromSrtmTile(scontext, tile);
                }
                pendingUpdates.remove(tile.getCoordinates());
            }
        }
    }


    @Override
    public void close() {
        scontext.getContext().unregisterReceiver(onFileChanged);
    }
}
