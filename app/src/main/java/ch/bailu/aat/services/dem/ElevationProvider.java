package ch.bailu.aat.services.dem;



public interface ElevationProvider {
    final static int NULL_ALTITUDE=0;

/*    final static ElevationProvider NULL=new ElevationProvider() {

        @Override
        public short getElevation(int laE6, int loE6) {
            return NULL_ALTITUDE;
        }
        
    };*/
    
    public short getElevation(int laE6, int loE6);
    
    

}
