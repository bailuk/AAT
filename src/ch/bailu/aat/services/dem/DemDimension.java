package ch.bailu.aat.services.dem;


public class DemDimension {
    public final int DIM;
    public final int DIM_OFFSET;
    public final int OFFSET;
    public final int SIZE;
    public final int LAST_INDEX;
    public final int LAST_IN_ROW;
    
    
    public DemDimension(int dim, int offset) {
        DIM=dim;
        DIM_OFFSET=dim+offset;
        OFFSET=offset;
        
        SIZE=DIM_OFFSET*DIM_OFFSET;
        LAST_INDEX=SIZE-1;
        LAST_IN_ROW=DIM-1;
    
    }
    
    /*
    public int limit(int index) {
        index=Math.max(0, index);
        index=Math.min(LAST_INDEX, index);
        return index;
    }
    */
    public int toPos(int laE6, int loE6) {
        final int x=toXPos(loE6);
        final int y=toYPos(laE6);

        return (y*DIM_OFFSET + x);
    }

    public int toXPos(int loE6) {
        if (loE6<0) return inverse(toPos(loE6)); // offset is in last column (index starts with 0)
        return toPos(loE6);
    }
    

    public int toYPos(int laE6) {
        if (laE6 >0) 
            return OFFSET + inverse(toPos(laE6)); // offset is in first row (index starts with 1)
        return OFFSET + toPos(laE6);
    }
    
    private int toPos(int cE6) {
        double c = Math.abs(cE6);
        
        c = c / 1e6d;
        
        final double deg = (int)c;

        final double min  = (c-deg);
        final double x = min * LAST_IN_ROW; // we use last index!!!
        
        
        return (int)x;
    }
    
    private int inverse(int v) {
        return LAST_IN_ROW-v;     
    }
}
