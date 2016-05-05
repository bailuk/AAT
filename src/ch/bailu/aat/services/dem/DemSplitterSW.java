package ch.bailu.aat.services.dem;

public class DemSplitterSW implements DemProvider {
    private final DemProvider parent;
    private final DemDimension dim;
    private final DemDimension parent_dim;

    private final float cellsize;

    public DemSplitterSW(DemProvider p) {
        parent=p;
        parent_dim=parent.getDim();
        cellsize=parent.getCellsize()/2;
        dim=new DemDimension(
                parent_dim.DIM*2, 
                parent_dim.OFFSET*2);
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
        final float div=2;

        /**
         *  a b
         *  C d
         *  
         *  [0,0] [0,1]
         *  [1,0] [1,1]
         *  
         */

        final int B = parent.getElevation(parent_index);
        if (row_mode+col_mode == 0) { // a
            final int a = parent.getElevation(parent_index - 1);
            sum = B+a;
        } else if (row_mode==0) {    // b
            sum = B+B;
        } else if (col_mode==0) {    // c
            final int c = parent.getElevation(parent_index+parent_dim.DIM_OFFSET-1);
            sum=B+c;
        } else {                     // d
            final int d = parent.getElevation(parent_index +parent_dim.DIM_OFFSET);
            sum = B+d;
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
