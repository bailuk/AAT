package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.dem.DemDimension;
import ch.bailu.aat.services.dem.DemGeoToIndex;
import ch.bailu.aat.services.dem.DemProvider;
import ch.bailu.aat.services.dem.DemSplitter;
import ch.bailu.aat.services.dem.MultiCell;
import ch.bailu.aat.services.dem.MultiCell8;

public class Hillshade8 extends NewHillshade {

    public Hillshade8(String id, SelfOn self, MapTile t) {
        super(id, self, t);
    }

    
    @Override
    public DemGeoToIndex factoryGeoToIndex(DemDimension dim) {
        return new DemGeoToIndex(dim, true);
    }

    @Override
    public DemProvider factorySplitter(DemProvider dem) {
        return new DemSplitter(dem);
    }

    @Override
    public MultiCell factoryMultiCell(DemProvider dem) {
        return new MultiCell8(dem);
    }

    
    public static class Factory extends ObjectHandle.Factory {
        private final MapTile mapTile;

        public Factory(MapTile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            return  new Hillshade8(id, self, mapTile);
        }
        
    } 
}
