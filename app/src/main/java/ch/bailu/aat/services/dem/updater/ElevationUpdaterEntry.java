package ch.bailu.aat.services.dem.updater;

import android.util.SparseArray;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.dem.tile.Dem3Tile;

public class ElevationUpdaterEntry {
    private final SparseArray <SrtmCoordinates> tiles = new SparseArray<>(5);
    private final String id;
    private final ServiceContext scontext;
 
    
    public ElevationUpdaterEntry(ServiceContext sc, String i) {
        id = i;
        scontext=sc;

        fillSRTMTiles();
    }


    private void fillSRTMTiles() {
        ObjectHandle handle = scontext.getCacheService().getObject(id);
            
        if (ElevationUpdaterClient.class.isInstance(handle)) {
    
            ElevationUpdaterClient updatable = (ElevationUpdaterClient) handle;
            SrtmCoordinates c[] = updatable.getSrtmTileCoordinates();
            for (SrtmCoordinates aC : c) {
                addSRTMTile(aC);
            }
        }
        
        handle.free();
    }
    

    private void addSRTMTile(SrtmCoordinates c) {
        if (c.toFile(scontext.getContext()).isReachable()) {
            tiles.put(c.hashCode(),c);
        }
    }
    
    

    
    public SrtmCoordinates getTile(int i) {
        if (tiles.size()>i)       
            return tiles.valueAt(i);
        
        return null;
    }
    
    public void update(ServiceContext sc, Dem3Tile tile) {

        final SrtmCoordinates c = tiles.get(tile.hashCode());

        
        if (c != null) {
            
            final ObjectHandle handle = scontext.getCacheService().getObject(id);

            if (ElevationUpdaterClient.class.isInstance(handle)) {
                ((ElevationUpdaterClient) handle).updateFromSrtmTile(sc, tile);
                tiles.delete(tile.hashCode());
            
            } else {
                tiles.clear();
            }
            
            handle.free();
        }
    }
}
