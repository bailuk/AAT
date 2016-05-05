package ch.bailu.aat.services.dem;


public class DemSplitter implements DemProvider {
    private final DemProvider parent;
    private final DemDimension dim;
    private final DemDimension parent_dim;
    
    private final float cellsize;

    public DemSplitter(DemProvider p) {
        parent=p;
        dim=new DemDimension(p.getDim().DIM*2, p.getDim().OFFSET*2);
        parent_dim=p.getDim();
        cellsize=p.getCellsize()/2;
    }

    public short getElevation(int index) {
        final int row = index / dim.DIM_OFFSET;
        final int col = index % dim.DIM_OFFSET;

        final int parent_row=row/2;
        final int parent_col=col/2;

        final int parent_index=parent_row*parent_dim.DIM_OFFSET + parent_col;

        final int row_mode=row % 2; 
        final int col_mode=col % 2;

        float sum=0;
        float div=1;
        
        /**
         *  a b
         *  C d
         *  
         *  [0,0] [0,1]
         *  [1,0] [1,1]
         *  
         */
        if (row_mode+col_mode == 0) { // a
            final int C = parent.getElevation(parent_index);
            final int a = parent.getElevation(parent_index - parent_dim.DIM_OFFSET);
            sum = a + C;
            div = 2;
            
        } else if (row_mode==0) {    // b
            final int C = parent.getElevation(parent_index);
            final int d = parent.getElevation(parent_index - parent_dim.DIM_OFFSET+1);
            sum = C + d;
            div = 2;
            

        } else if (col_mode==0) {    // c
            final int C = parent.getElevation(parent_index);
            sum=C;
            div=1;

        } else {                     // d
            final int C = parent.getElevation(parent_index);
            final int d = parent.getElevation(parent_index + 1);
            sum = C + d;
            div = 2;
        }
        
        return (short)Math.round(sum / div);
    }

  
    @Override
    public DemDimension getDim() {
        return dim;
    }

    @Override
    public float getCellsize() {
        return cellsize;
    }

    @Override
    public boolean inverseLatitude() {
        return parent.inverseLatitude();
    }

    @Override
    public boolean inverseLongitude() {
        return parent.inverseLongitude();
    }
}
