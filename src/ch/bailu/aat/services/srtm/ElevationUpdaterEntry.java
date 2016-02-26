package ch.bailu.aat.services.srtm;

import android.util.SparseArray;
import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.ObjectHandle;

public class ElevationUpdaterEntry {
    private final SparseArray <SrtmCoordinates> tiles = new SparseArray<SrtmCoordinates>(5);
    private final String id;
    private final CacheService loader;
 
    
    public ElevationUpdaterEntry(CacheService l, String i) {
        id = i;
        loader = l;

        fillSRTMTiles();
    }

    public String getFileObjectID() {
        return id;
    }

    private void fillSRTMTiles() {
        ObjectHandle handle = loader.getObject(id);
            
            
        if (ElevationUpdaterClient.class.isInstance(handle)) {
    
            ElevationUpdaterClient updatable =  (ElevationUpdaterClient) handle;
            SrtmCoordinates c[] = updatable.getSrtmTileCoordinates();
            for (int i = 0; i< c.length; i++) {
                addSRTMTile(c[i]);
            }
        }
        
        handle.free();
    }
    

    private void addSRTMTile(SrtmCoordinates c) {
        if (c.toFile(loader).exists()) {
            tiles.put(c.hashCode(),c);
        }
    }
    
    

    
    public SrtmCoordinates getTile(int i) {
        if (tiles.size()>i)       
            return tiles.valueAt(i);
        
        return null;
    }
    
    public void update(BackgroundService bg, Dem3Tile tile) {

        final SrtmCoordinates c = tiles.get(tile.hashCode());

        
        if (c != null) {
            
            final ObjectHandle handle = loader.getObject(id);
            
            

            if (ElevationUpdaterClient.class.isInstance(handle)) {
                ((ElevationUpdaterClient) handle).updateFromSrtmTile(bg, tile);
                tiles.delete(tile.hashCode());
            
            } else {
                tiles.clear();
            }
            
            handle.free();
        }
        
    }

  
}
