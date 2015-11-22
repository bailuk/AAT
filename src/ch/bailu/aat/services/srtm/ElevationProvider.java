package ch.bailu.aat.services.srtm;

import org.osmdroid.api.IGeoPoint;


public interface ElevationProvider {
    public boolean isReady();
    public short getElevation(int laE6, int loE6);
    public short getElevation(IGeoPoint p);
    public short getElevation(int index);
    
    
}
