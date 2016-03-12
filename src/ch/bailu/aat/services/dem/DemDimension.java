package ch.bailu.aat.services.dem;

public class DemDimension {
    public final int DIM;
    public final int DIM_OFFSET;
    public final int OFFSET;
    
    
    
    public DemDimension(int dim, int offset) {
        DIM=dim;
        DIM_OFFSET=dim+offset;
        OFFSET=offset;
    
    }
    

    public int toPos(int laE6, int loE6) {
        int x=toXPos(loE6);
        int y=toYPos(laE6);

        return (y*DIM_OFFSET + x);
    }

    public int toXPos(int loE6) {
        if (loE6<0) return inverse(toPos(loE6));
        return toPos(loE6);
    }
    

    public int toYPos(int laE6) {
        if (laE6 >0) return inverse(toPos(laE6));
        return toPos(laE6);
    }
    
    private int toPos(int cE6) {
        double c = Math.abs(cE6);
        
        c = c / 1e6d;
        
        final double deg = (int)c;

        final double min  = (c-deg);
        final double x = min * DIM;
        return (int)x;
    }
    
    private int inverse(int v) {
        return DIM-v;    
    }

}
