package ch.bailu.aat.services.dem;



public interface ElevationProvider {
    final static int NULL_ALTITUDE=0;


    public short getElevation(int laE6, int loE6);
    
    

}
