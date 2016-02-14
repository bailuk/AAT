package ch.bailu.aat.services.cache;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.srtm.ElevationProvider;


public class ElevationProviderObject extends ObjectHandle implements ElevationProvider {
   
    
    public ElevationProviderObject(String id) {
        super(id);
        
    }



    @Override
    public long getSize() {
        return 1024;
    }


    @Override
    public boolean isReady() {
        return true;
    }

    
    

    public static class Factory extends ObjectHandle.Factory {
        private final String url;

        public Factory(SrtmCoordinates coordinates) {
            
            
            url = coordinates.toURL();
        }


        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            ObjectHandle o=null;
            if (url != null) {
                try {
                    o=new Srtmgl3TileObject(id, self, url);
                } catch (OutOfMemoryError E) {
                    o=new ElevationProviderObject(id);
                }
            } else {
                o=new ElevationProviderObject(id);
            }
            
            return o;
        }
    }


    @Override
    public void onDownloaded(String id, String url, SelfOn self) {}


    @Override
    public void onChanged(String id,  SelfOn self) {}



    @Override
    public short getElevation(int laE6, int loE6) {
        return 0;
    }



    @Override
    public short getElevation(IGeoPoint p) {
        return getElevation(p.getLatitudeE6(), p.getLongitudeE6());
    }



    @Override
    public short getElevation(int index) {
        return 0;
    }
};
