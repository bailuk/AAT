package ch.bailu.aat.services.srtm;

import java.io.Closeable;

import org.osmdroid.api.IGeoPoint;

public class SrtmAccess implements ElevationProvider, Closeable {
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
    public void close() {}


    public short getElevation(IGeoPoint point) {
        return getElevation(point.getLatitudeE6(), point.getLongitudeE6());
        
    }


    @Override
    public short getElevation(int index) {
        return SRTM.NULL_ALTITUDE;
    }
} 