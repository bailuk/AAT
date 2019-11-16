package ch.bailu.aat.services.dem.tile;

public class DemSplitterNE extends DemSplitter {

    public DemSplitterNE(DemProvider p) {
        super(p);
    }

    @Override
    public short getElevation(int index) {
        final int row = index / dim;
        final int col = index % dim;

        final int parent_row=row/2;
        final int parent_col=col/2;

        final int parent_index=parent_row*parent_dim + parent_col;

        final int row_mode=row % 2;
        final int col_mode=col % 2;

        int sum;
        int div=2;

        final int C = parent.getElevation(parent_index);

        if (row_mode+col_mode == 0) { // a
            final int a = parent.getElevation(parent_index - parent_dim);
            sum = C + a;

        } else if (row_mode==0) {    // b
            final int b = parent.getElevation(parent_index - parent_dim + 1);
            sum = C + b;
        } else if (col_mode==0) {    // c
            sum=C;
            div=1;
        } else {                     // d
            final int d = parent.getElevation(parent_index + 1);
            sum = C + d;

        }
        return (short)(sum / div);
    }
}
