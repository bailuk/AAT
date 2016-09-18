package ch.bailu.aat.views.map;

import java.io.Closeable;
import java.util.ArrayList;

import org.osmdroid.tileprovider.MapTile;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.TileStackObject;

public class PreviewTileProvider extends AbsOsmTileProvider implements Closeable  {

    private final ArrayList<TileStackObject> tiles = new ArrayList<>(10);
    

    public PreviewTileProvider(ServiceContext sc) {
        super(sc);
    }

    
    @Override
    public Drawable getMapTile(MapTile tile) {
        final TileStackObject handle = getTileHandle(tile);
        
        tiles.add(handle);
       
        return handle.getDrawable();
    }


    @Override
    public void detach() {}

    @Override
    public void setTileRequestCompleteHandler(Handler handler) {

    }

    @Override
    public void ensureCapacity(int numNeeded) {}

    @Override
    public void deleteVisibleTilesFromDisk() {}


    public boolean isReady() {
        
        for (int i=0; i<tiles.size(); i++) {
            if (tiles.get(i).isReady()==false) return false;
        }
        return true;
    }


    @Override
    public void close() {
        for (int i=0; i<tiles.size(); i++) {
            tiles.get(i).free();
        }
        tiles.clear();
    }
}


