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
            tiles.put(c.toString().hashCode(),c);
        }
    }
    
    

    
    public SrtmCoordinates getNextSRTMTile() {
        if (tiles.size()>0)       
            return tiles.valueAt(0);
        
        return null;
    }
    
    public void update(BackgroundService bg, SrtmAccess srtm) {

        final SrtmCoordinates c = tiles.get(srtm.toString().hashCode());

        
        if (c != null) {
            final ObjectHandle handle = loader.getObject(id);

            tiles.delete(srtm.toString().hashCode());

            if (ElevationUpdaterClient.class.isInstance(handle)) {
                ((ElevationUpdaterClient) handle).updateFromSrtmTile(bg, srtm);
            
            } else {
                tiles.clear();
            }
            
            handle.free();

        }
        
    }

    public boolean isUpdating() {
        boolean r=false;
        ObjectHandle handle = loader.getObject(id);

        if (ElevationUpdaterClient.class.isInstance(handle)) {
            r = ((ElevationUpdaterClient) handle).isUpdating();
        }
        handle.free();
        return r;
    }
}
