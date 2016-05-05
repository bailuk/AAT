package ch.bailu.aat.services.dem;


public class DemSplitter implements DemProvider {
    private final DemProvider parent;
    private final DemDimension dim;
    private final DemDimension parent_dim;

    private final float cellsize;

    
    public static DemProvider factory(DemProvider dem) {
        if (dem.inverseLatitude()==true && dem.inverseLongitude()==false) { // NE
            return new DemSplitterNE(dem);

        } else if (dem.inverseLatitude()==false && dem.inverseLongitude()==false) { // SE{
            return new DemSplitterSE(dem);

        } else if (dem.inverseLatitude()==false && dem.inverseLongitude()==true) { // SW{
            return new DemSplitterSW(dem);
            
        } else { // NW
            return new DemSplitterNW(dem);
        }
        
    }
    
    private DemSplitter(DemProvider p) {
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

        if (parent.inverseLatitude()==true && parent.inverseLongitude()==false) { // NE
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

        } else if (parent.inverseLatitude()==false && parent.inverseLongitude()==false) { // SE
            final int A = parent.getElevation(parent_index);
            if (row_mode+col_mode == 0) { // a
                sum = A+A;
            } else if (row_mode==0) {    // b
                final int b = parent.getElevation(parent_index + 1);
                sum = A + b;
            } else if (col_mode==0) {    // c
                final int c = parent.getElevation(parent_index+parent_dim.DIM_OFFSET);
                sum=A+c;
            } else {                     // d
                final int d = parent.getElevation(parent_index +parent_dim.DIM_OFFSET+ 1);
                sum = A+d;
            }
        } else if (parent.inverseLatitude()==false && parent.inverseLongitude()==true) { // SW
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
        } else  {// NW
            final int D = parent.getElevation(parent_index);
            if (row_mode+col_mode == 0) { // a
                final int a = parent.getElevation(parent_index - parent_dim.DIM_OFFSET-1);
                sum = D+a;
            } else if (row_mode==0) {    // b
                final int b = parent.getElevation(parent_index - parent_dim.DIM_OFFSET);
                sum = D+b;
            } else if (col_mode==0) {    // c
                final int c = parent.getElevation(parent_index - 1);
                sum=D+c;
            } else {                     // d
                sum = D+D;
            }
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
