package ch.bailu.aat.services.dem.updater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import java.io.Closeable;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.dem.loader.Dem3Loader;
import ch.bailu.aat.services.dem.tile.Dem3Tile;
import ch.bailu.aat.services.dem.loader.Dem3Tiles;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.services.ServiceContext;


public class ElevationUpdater implements Closeable {
    private final SparseArray <ElevationUpdaterEntry> pendingObjects = new SparseArray<>();
    private final ServiceContext scontext;

    private final Dem3Loader loader;
    private final Dem3Tiles tiles;

    public ElevationUpdater(ServiceContext cs, Dem3Loader d, Dem3Tiles t) {
        scontext=cs;
        tiles = t;
        loader = d;

        final Context c = cs.getContext();

        AppBroadcaster.register(c, onRequestElevationUpdate, AppBroadcaster.REQUEST_ELEVATION_UPDATE);
        AppBroadcaster.register(c, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
    }



    private final BroadcastReceiver onRequestElevationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppIntent.getFile(intent);
            
            addObject(id);
            updateObject(id);
            loadTiles();
        }
    };



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


    private void addObject(String id) {
        pendingObjects.put(id.hashCode(), new ElevationUpdaterEntry(scontext, id));
    }
    
    
    private void loadTiles() {
        SrtmCoordinates c;
        
        for (int i = pendingObjects.size()-1; i>-1; i--) {
            int x=0;
            while((c = pendingObjects.valueAt(i).getTile(x)) != null) {
                if (loader.requestDem3Tile(c) == null) return;
                x++;
            }
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
            for (int i = pendingObjects.size()-1; i>-1; i--) {
                updateObject(i, tile);
            }
        }
    }
    

    
    private void updateObject(int i, Dem3Tile tile) {
        ElevationUpdaterEntry entry = pendingObjects.valueAt(i);
        
        entry.update(scontext, tile);
        
        if (entry.getTile(0) == null) {
            pendingObjects.remove(pendingObjects.keyAt(i)); 
        }
    }

    
    private void updateObject(String id) {
        int t=0;
        Dem3Tile tile;
        while ((tile=tiles.get(t)) != null) {
            if (tile.isLoaded()) {
                updateObject(id, tile);
            }
            t++;
        }
    }
    
    
    private void updateObject(String id, Dem3Tile tile) {
        ElevationUpdaterEntry entry = pendingObjects.get(id.hashCode());
        
        if (entry != null) {
            entry.update(scontext, tile);
            if (entry.getTile(0) == null) {
                pendingObjects.remove(id.hashCode()); 
            }
        }
    }

    
    
    @Override
    public void close() {
        final Context c = scontext.getContext();

        c.unregisterReceiver(onRequestElevationUpdate);
        c.unregisterReceiver(onFileChanged);
    }

}
