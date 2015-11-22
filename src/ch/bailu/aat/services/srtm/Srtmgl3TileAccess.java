package ch.bailu.aat.services.srtm;

import java.io.File;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.ElevationProviderObject;

public class Srtmgl3TileAccess extends SrtmAccess{

    private final ElevationProviderObject tile;
    private final CacheService loader;
    
    
    private final File   file;
    private final String ID;
    
    
    
    public Srtmgl3TileAccess(IGeoPoint p, CacheService ls) {
        this(new SrtmCoordinates(p), ls);
    }
    
    
    
    public Srtmgl3TileAccess(SrtmCoordinates coordinates, CacheService ls) {
        loader = ls;
        
        ID = coordinates.toString();
        
        file = coordinates.toFile(loader);
        
        tile = (ElevationProviderObject) loader.getObject(file.getAbsolutePath(), new ElevationProviderObject.Factory(coordinates));
    }


    @Override
    public String toString() {
        return ID;
    }
    
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public boolean isReady() {
        return tile.isReady();
    }

    @Override
    public short getElevation(int la, int lo) {
        return tile.getElevation(la, lo);
    }

    @Override
    public short getElevation(int index) {
        return tile.getElevation(index);
    }


    @Override
    public void cleanUp() {
        tile.free();
    }
}
