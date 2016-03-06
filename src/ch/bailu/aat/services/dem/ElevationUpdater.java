package ch.bailu.aat.services.dem;

import java.io.Closeable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;


public class ElevationUpdater implements Closeable, ElevationProvider{
    private final SparseArray <ElevationUpdaterEntry> pendingObjects = new SparseArray<ElevationUpdaterEntry>();
    private final CacheService cache;
    private final BackgroundService background;
    private final Context context;

    private final Dem3Tiles tiles;

    protected ElevationUpdater(CacheService c, BackgroundService b) {
        cache = c;
        tiles =new Dem3Tiles(b);
        context = c;
        background = b;

        AppBroadcaster.register(c, onRequestElevationUpdate, AppBroadcaster.REQUEST_ELEVATION_UPDATE);
        AppBroadcaster.register(c, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
        AppBroadcaster.register(c, onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);
    }



    private BroadcastReceiver onRequestElevationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppBroadcaster.getFile(intent);
            
            addObject(id);
            updateObject(id);
            loadTiles();
        }
    };


    private BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppBroadcaster.getFile(intent);
            Dem3Tile tile = tiles.get(id);
            
            if (tile != null) {
                tile.reload(background);
            }
        }
    };

    private BroadcastReceiver onFileChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppBroadcaster.getFile(intent);
            
            if (tiles.have(id)){
                updateObjects();
            }
            loadTiles();
        }
    };


    private void addObject(String id) {
        pendingObjects.put(id.hashCode(), new ElevationUpdaterEntry(cache, id));
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
        
        entry.update(background, tile);
        
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
            entry.update(background, tile);
            if (entry.getTile(0) == null) {
                pendingObjects.remove(id.hashCode()); 
            }
        }
    }

    
    
    @Override
    public void close() {
        context.unregisterReceiver(onRequestElevationUpdate);
        context.unregisterReceiver(onFileChanged);
        context.unregisterReceiver(onFileDownloaded);
    }


    @Override
    public short getElevation(int laE6, int loE6) {
        return tiles.getElevation(laE6, loE6);
    }
}
