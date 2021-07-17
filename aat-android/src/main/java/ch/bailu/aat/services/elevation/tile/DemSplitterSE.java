package ch.bailu.aat.services.elevation.tile;

public final class DemSplitterSE extends DemSplitter {


    public DemSplitterSE(DemProvider p) {
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

        float sum;
        final float div=2;

        /*
           a b
           C d

           [0,0] [0,1]
           [1,0] [1,1]

         */
        final int A = parent.getElevation(parent_index);
        if (row_mode+col_mode == 0) { // a
            sum = A+A;
        } else if (row_mode==0) {    // b
            final int b = parent.getElevation(parent_index + 1);
            sum = A + b;
        } else if (col_mode==0) {    // c
            final int c = parent.getElevation(parent_index+parent_dim);
            sum=A+c;
        } else {                     // d
            final int d = parent.getElevation(parent_index +parent_dim+ 1);
            sum = A+d;
        }

        return (short)Math.round(sum / div);
    }
}
