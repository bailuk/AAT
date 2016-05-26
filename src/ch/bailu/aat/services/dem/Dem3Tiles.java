package ch.bailu.aat.services.dem;

import java.io.File;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.DownloadHandle;


public class Dem3Tiles {
    private class Loader {
        private static final long MILLIS=1000;
        private SrtmCoordinates toLoad = null;
        
        private final Runnable run = new Runnable() {
        @Override
        public void run() {
            // now we are idle
            if (toLoad != null) {
                SrtmCoordinates loadNow=toLoad;
                toLoad=null;
                load(loadNow);
                download(loadNow);
                
            }
        }
        };
        
        private final Timer timer = new Timer(run, MILLIS);
        
        
        public Dem3Tile load(SrtmCoordinates c) {
            timer.close();  // not idle
            if (toLoad != null) timer.kick();
            
            if (!have(c)) {
                Dem3Tile slot = getOldestProcessed();
                
                if (slot != null) {
                    slot.load(serviceContext, c);
                }
            }
            
            return get(c);    
        }
        
        
        public void loadIfIdle(SrtmCoordinates c) {
            if (toLoad == null) { // first request
                timer.close();
                timer.kick();
            }
            toLoad=c;
        }
        
        
        public void clearIdle() {
            toLoad = null;
            timer.close();
        }
        
        private void download(SrtmCoordinates c) {
            File target = c.toFile(serviceContext.getContext());
            if (target.exists()==false) {
                DownloadHandle handle = new DownloadHandle(c.toURL(), target);
                serviceContext.getBackgroundService().download(handle);
            }
        }
    }
    
    private final Loader loader= new Loader();
    
    private final static int NUM_TILES=1;
    
    private final Dem3Tile tiles[];
    private final ServiceContext serviceContext;
    
    public Dem3Tiles(ServiceContext c) {
        serviceContext=c;
        tiles = new Dem3Tile[NUM_TILES];
        for (int i=0; i< NUM_TILES; i++) tiles[i]=new Dem3Tile();
    }
    


    public short getElevation(int laE6, int loE6) {
        short r=0;
        SrtmCoordinates c = new SrtmCoordinates (laE6, loE6);
        Dem3Tile t=get(c);
        
        if (t == null) {
            loader.loadIfIdle(c);
        } else {
            loader.clearIdle();
            if (t.isLoaded()) {
                r=t.getElevation(laE6, loE6);
            }
        }
        return r;
    }
    
    

    public  Dem3Tile want(SrtmCoordinates c) {
        Dem3Tile t=get(c);
        
        if (t==null) {
            t=loader.load(c);
        }
        
        return t;
    }
    
    
    
    private Dem3Tile getOldestProcessed() {
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
            if (tiles[i].hashCode() == c.hashCode()) {
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

    
    public boolean have(String id) {
        return get(id) != null;
    }


    
    public boolean have(SrtmCoordinates c) {
        return get(c) != null;
    }
}
