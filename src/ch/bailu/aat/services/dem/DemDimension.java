package ch.bailu.aat.services.dem;



public class DemDimension {
    public final int DIM;
    public final int OFFSET;
    public final int SIZE;
    
    
    public DemDimension(int dim, int offset) {
        DIM=dim;
        OFFSET=offset;
        SIZE=DIM*DIM;
    }
    

    
}
