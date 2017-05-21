package ch.bailu.aat.services.dem.tile;

public interface DemProvider {
    short getElevation(int index);
    DemDimension getDim();
    
    float getCellsize();
    
    boolean inverseLatitude();
    boolean inverseLongitude();
}
