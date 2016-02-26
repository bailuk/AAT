package ch.bailu.aat.services.srtm;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.background.BackgroundService;


public class Dem3Tiles {
    private final static int NUM_TILES=1;
    
    private final Dem3Tile tiles[];
    private final BackgroundService background;
    
    public Dem3Tiles(BackgroundService c) {
        background=c;
        tiles = new Dem3Tile[NUM_TILES];
        for (int i=0; i< NUM_TILES; i++) tiles[i]=new Dem3Tile();
    }
    
    
    public short getElevation(int laE6, int loE6) {
        SrtmCoordinates c=new SrtmCoordinates(laE6, loE6);
        Dem3Tile t=want(c);
        
        if (t != null && t.isLoaded()) {
            return t.getElevation(laE6, loE6);
        } else {
            return 0;
        }
    }
    
    

    public  Dem3Tile want(SrtmCoordinates c) {
        Dem3Tile t=get(c);
        
        if (t==null) {
            t = getOldestProcessed();
            
            if (t != null) {
                t.load(background, c);
            }
        }
        
        return t;
    }
    
    
    public Dem3Tile getOldestProcessed() {
        Dem3Tile t=null;
        long stamp=System.currentTimeMillis();
        
        for (int i=0; i<NUM_TILES; i++) {
            if (tiles[i].isProcessed() && tiles[i].getTimeStamp() < stamp) {
                t=tiles[i];
                stamp=t.getTimeStamp();
                
            } else if (tiles[i].isLoading()) {
                return null;
            }
        }
        
        return t;
    }
    
    
    
    public Dem3Tile get(int index) {
        if (index < tiles.length) return tiles[index];
        return null;
    }
    
    public Dem3Tile get(SrtmCoordinates c) {
        for (int i=0; i<NUM_TILES; i++) {
            if (tiles[i].toString().equals(c.toString())) {
                return tiles[i];
            }
        }
        return null;
    }
    
    
    public Dem3Tile get(String id) {
        for (int i=0; i<NUM_TILES; i++) {
            if (id.contains(tiles[i].toString())) {
                return tiles[i];
            }
        }
        return null;
    }

    
    boolean have(String id) {
        return get(id) != null;
    }


    
    boolean have(SrtmCoordinates c) {
        return get(c) != null;
    }
}