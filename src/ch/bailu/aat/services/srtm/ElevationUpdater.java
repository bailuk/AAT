package ch.bailu.aat.services.srtm;

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
    }



    private BroadcastReceiver onRequestElevationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = AppBroadcaster.getFile(intent);
            
            addObject(id);
            updateObjects();
            loadTiles();
        }
    };



    private BroadcastReceiver onFileChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateObjects();
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
            
            if (tile.isLoaded()) {
                t++;
                for (int i = pendingObjects.size()-1; i>-1; i--) {
                    pendingObjects.valueAt(i).update(background, tile);
                    if (pendingObjects.valueAt(i).getTile(0) == null) {
                        pendingObjects.remove(pendingObjects.keyAt(i)); 
                    }
                }
            }
        }
    }
    
    

    @Override
    public void close() {
        context.unregisterReceiver(onRequestElevationUpdate);
        context.unregisterReceiver(onFileChanged);
    }


    @Override
    public short getElevation(int laE6, int loE6) {
        return tiles.getElevation(laE6, loE6);
    }
}
