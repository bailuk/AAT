package ch.bailu.aat.services.dem;

public interface DemProvider {
    short getElevation(int index);
    DemDimension getDim();
    
    float getCellsize();
    
    boolean inverseLatitude();
    boolean inverseLongitude();
}
