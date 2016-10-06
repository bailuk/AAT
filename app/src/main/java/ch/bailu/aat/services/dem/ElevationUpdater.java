package ch.bailu.aat.services.dem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import java.io.Closeable;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.services.ServiceContext;


public class ElevationUpdater implements Closeable, ElevationProvider{
    private final SparseArray <ElevationUpdaterEntry> pendingObjects = new SparseArray<>();
    private final ServiceContext scontext;
    private final ServiceContext serviceContext;

    private final Dem3Tiles tiles;

    protected ElevationUpdater(ServiceContext cs) {
        scontext=cs;
        tiles =new Dem3Tiles(cs);
        serviceContext = cs;

        final Context c = cs.getContext();
        AppBroadcaster.register(c, onRequestElevationUpdate, AppBroadcaster.REQUEST_ELEVATION_UPDATE);
        AppBroadcaster.register(c, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
        AppBroadcaster.register(c, onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);
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


    private final BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppIntent.getFile(intent);
            Dem3Tile tile = tiles.get(id);
            
            if (tile != null) {
                tile.reload(serviceContext);
            }
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
                if (tiles.want(c)==null) return;
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
        final Context c = serviceContext.getContext();

        c.unregisterReceiver(onRequestElevationUpdate);
        c.unregisterReceiver(onFileChanged);
        c.unregisterReceiver(onFileDownloaded);
    }


    @Override
    public short getElevation(int laE6, int loE6) {
        return tiles.getElevation(laE6, loE6);
    }
}
