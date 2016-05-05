package ch.bailu.aat.services.dem;

public class DemSplitterNE implements DemProvider {
    private final DemProvider parent;
    private final DemDimension dim;
    private final DemDimension parent_dim;

    private final float cellsize;

    public DemSplitterNE(DemProvider p) {
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

        float sum;
        final float div=2;

        final int C = parent.getElevation(parent_index);

        if (row_mode+col_mode == 0) { // a
            final int a = parent.getElevation(parent_index - parent_dim.DIM_OFFSET);
            sum = C + a;
        } else if (row_mode==0) {    // b
            final int d = parent.getElevation(parent_index - parent_dim.DIM_OFFSET+1);
            sum = C + d;
        } else if (col_mode==0) {    // c
            sum=C + C;
        } else {                     // d
            final int d = parent.getElevation(parent_index + 1);
            sum = C + d;
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
