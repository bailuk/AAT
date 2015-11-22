package ch.bailu.aat.views.map;

import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.helpers.CleanUp;
import ch.bailu.aat.services.cache.TileStackObject;

public class TileCache implements CleanUp {
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
        for (int i = 0; i<tiles.length; i++) {
            if (tiles[i].toString().equals(string)) {
                return tiles[i];
            }
        }
        return null;
    }


    public TileStackObject getFromSubTile(String id) {
        for (int i = 0; i<tiles.length; i++) {
            if (tiles[i].isInStack(id)) {
                return tiles[i];
            }
        }
        return null;
    }
    

    public TileStackObject get(MapTile tile) {
        for (int i = 0; i<tiles.length; i++) {
            if (tile.equals(tiles[i].getTile())) {
                tiles[i].access();
                return tiles[i];
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
    public void cleanUp() {
        reset();
    }


    public void deleteFromDisk() {
        for (int i=0; i<tiles.length; i++) {
            
            tiles[i].deleteFromDisk();
        }
        reset();
    }
    
    
    public void reset() {
        for (int i=0; i<tiles.length; i++) {
            tiles[i].free();
            tiles[i] = TileStackObject.NULL;
        }
    }


    public void setCapacity(int capacity) {
        if (capacity > tiles.length) {
            createCache(capacity);
        }
        
    }
    
    private void createCache(int capacity) {
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
