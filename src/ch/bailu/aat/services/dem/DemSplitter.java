package ch.bailu.aat.services.dem;

public class DemSplitter implements DemProvider {
    private final DemProvider parent;
    private final DemDimension dim;
    private final DemDimension parent_dim;

    public DemSplitter(DemProvider p) {
        parent=p;
        dim=new DemDimension(p.getDim().DIM*2, p.getDim().DIM_OFFSET*2, p.getDim().METER_PER_PIXEL/2);
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
