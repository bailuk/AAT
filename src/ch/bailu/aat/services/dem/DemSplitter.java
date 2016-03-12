package ch.bailu.aat.services.dem;

public class DemSplitter implements DemProvider {
    private final DemProvider parent;
    private final DemDimension dim;
    private final DemDimension parent_dim;

    public DemSplitter(DemProvider p) {
        parent=p;
        dim=new DemDimension(p.getDim().DIM*2, p.getDim().DIM_OFFSET*2);
        parent_dim=p.getDim();
    }

    public short getElevation(int index) {
        final int row = index / dim.DIM_OFFSET;
        final int col = index % dim.DIM_OFFSET;

        final int parent_row=row/2;
        final int parent_col=col/2;

        final int parent_index=parent_row*parent_dim.DIM_OFFSET + parent_col;


        final int row_mode=row % 2;
        final int col_mode=col % 2;


        int sum=0;
        int div=1;
        
        if (row_mode+col_mode ==0) { // a
            final int A = parent.getElevation(parent_index);
            sum=A;
            div=1;
            
        } else if (row_mode==0) {    // b
            final int A = parent.getElevation(parent_index);
            final int B = parent.getElevation(parent_index + 1);
            sum = A + B;
            div = 2;

        } else if (col_mode==0) {    // c
            final int A = parent.getElevation(parent_index);
            final int C = parent.getElevation(parent_index + parent_dim.DIM_OFFSET);
            sum = A + C;
            div = 2;

        } else {                     // d
            final int A = parent.getElevation(parent_index);
            final int B = parent.getElevation(parent_index + 1);
            final int C = parent.getElevation(parent_index + parent_dim.DIM_OFFSET);
            final int D = parent.getElevation(parent_index + parent_dim.DIM_OFFSET+1);
            sum = A + B + C + D;
            div = 4;
        }
        
        return (short) Math.round(sum / div);
    }

   

    
    public short getElevationOld(int index) {
        final int row = index / dim.DIM_OFFSET;
        final int col = index % dim.DIM_OFFSET;

        final int parent_row=row/2;
        final int parent_col=col/2;

        final int parent_index=parent_row*parent_dim.DIM_OFFSET + parent_col;


        final int row_mode=row % 2;
        final int col_mode=col % 2;

        if (row_mode+col_mode ==0) {
            return parent.getElevation(parent_index);

        } else if (col_mode==0) {
            final short e1 = parent.getElevation(parent_index);
            final short e2 = parent.getElevation(parent_index + parent_dim.DIM_OFFSET);
            return (short) ((e1 + e2) /2);

        } else if (row_mode==0) { 
            final short e1 = parent.getElevation(parent_index);
            final short e2 = parent.getElevation(parent_index + 1);
            return (short) ((e1 + e2 ) /2);

        } else {
            final short e1 = parent.getElevation(parent_index);
            final short e2 = parent.getElevation(parent_index + parent_dim.DIM_OFFSET+1);
            
            return (short) ((e1+e2) /2);
        }
    }

    @Override
    public DemDimension getDim() {
        return dim;
    }
}
