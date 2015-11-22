package ch.bailu.aat.services.srtm;

import org.osmdroid.util.GeoPoint;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.cache.CacheService;

public class SRTMGL3GeneralAccess extends SrtmAccess {
    private SrtmAccess srtmAccess=new SrtmAccess();
    private final CacheService loader;

    
    
    public SRTMGL3GeneralAccess(CacheService l) {
        loader=l;
    }
    
    
    @Override
    public short getElevation(int la, int lo) {
        SrtmCoordinates c = new SrtmCoordinates(new GeoPoint(la, lo));
        
        if (needsNewTile(c))
            loadNewTile(c);
        
        return srtmAccess.getElevation(la, lo);
    }
    
    
    @Override
    public short getElevation(int index) {
        return  srtmAccess.getElevation(index);
    }

    
    @Override
    public String toString() {
        return srtmAccess.toString(); 
    }
    
    @Override
    public int hashCode() {
        return srtmAccess.hashCode();
    }

    
    private void loadNewTile(SrtmCoordinates c) {
        try {
            srtmAccess.cleanUp();
            srtmAccess = new Srtmgl3TileAccess(c, loader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private boolean needsNewTile(SrtmCoordinates c) {
        return (srtmAccess.toString().equals(c.toString()) == false);
    }


    @Override
    public boolean isReady() {
        return srtmAccess.isReady();
    }
    
    @Override
    public void cleanUp() {
        srtmAccess.cleanUp();
    }
    
}
