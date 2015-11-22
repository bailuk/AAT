package ch.bailu.aat.services.srtm;

import org.osmdroid.api.IGeoPoint;

import ch.bailu.aat.helpers.CleanUp;

public class SrtmAccess implements ElevationProvider, CleanUp {
    //public static final SrtmAccess NULL = new SrtmAccess();
    public static final SrtmAccess NULL_READY = new SrtmAccess() {
        @Override
        public boolean isReady() {
            return true;
        }
    };
    
    
    @Override
    public boolean isReady() {
        return false;
    }

    
    @Override
    public short getElevation(int la, int lo) {
        return SRTM.NULL_ALTITUDE;
    }

    @Override
    public void cleanUp() {}


    public short getElevation(IGeoPoint point) {
        return getElevation(point.getLatitudeE6(), point.getLongitudeE6());
        
    }


    @Override
    public short getElevation(int index) {
        return SRTM.NULL_ALTITUDE;
    }
} 