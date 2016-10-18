package ch.bailu.aat.views.map;

import org.osmdroid.tileprovider.MapTile;

import java.io.Closeable;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.TileStackObject;

public class TileCache implements Closeable {
    private TileStackObject[] tiles;


    public TileCache(int capacity) {
        tiles = new TileStackObject[capacity];
        for (int i = 0; i< tiles.length; i++) 
            tiles[i] = TileStackObject.NULL;
    }


    public int getCapacity() {
        return tiles.length;
    }

    
    public TileStackObject get(String string) {
        for (TileStackObject tile : tiles) {
            if (tile.toString().equals(string)) {
                return tile;
            }
        }
        return null;
    }


    public TileStackObject getFromSubTile(String id) {
        for (TileStackObject tile : tiles) {
            if (tile.isInStack(id)) {
                return tile;
            }
        }
        return null;
    }
    

    public TileStackObject get(MapTile tile) {
        for (TileStackObject t : tiles) {
            if (tile.equals(t.getTile())) {
                t.access();
                return t;
            }
        }
        return null;
    }



    public void put(TileStackObject handle) {
        int i = indexOfOldest();

        tiles[i].free();
        tiles[i] = handle;
    }



    private int indexOfOldest() {
        int x=0;
        for (int i = 1; i<tiles.length; i++) {
            if (tiles[i].getAccessTime() < tiles[x].getAccessTime()) {
                x=i;
            }
        }
        return x;
    }


    @Override
    public void close() {
        reset();
    }


    public void reDownloadTiles(ServiceContext sc) {
        for (int i=0; i<tiles.length; i++) {
            
            tiles[i].reDownload(sc);
        }
    }
    
    
    public void reset() {
        for (int i=0; i<tiles.length; i++) {
            tiles[i].free();
            tiles[i] = TileStackObject.NULL;
        }
    }


    public void setCapacity(int capacity) {
        if (capacity > tiles.length) {
            resizeCache(capacity);
        }
        
    }
    
    private void resizeCache(int capacity) {
        final TileStackObject[] newTiles=new TileStackObject[capacity];
        final int l = Math.min(newTiles.length, tiles.length);
        int x,i;
        
        for (i=0; i<l; i++) {
            newTiles[i]=tiles[i];
        }

        for (x=i; x<newTiles.length; x++) {
            newTiles[x]=TileStackObject.NULL;
        }
        
        for (x=i; x<tiles.length; x++) {
            tiles[x].free();
        }
        tiles=newTiles;
    }
}
