package ch.bailu.aat_lib.service.elevation.tile;


public class DemSplitter implements DemProvider {
    public final DemProvider parent;
    public final int dim, parent_dim;

    private final DemDimension _dim;

    private final float cellsize;


    public static DemProvider factory(DemProvider dem) {

        if (dem.inverseLatitude()==true && dem.inverseLongitude()==false) {
            dem = new DemSplitterNE(dem);

        } else if (dem.inverseLatitude()==false && dem.inverseLongitude()==false) {
            dem = new DemSplitterSE(dem);

        } else if (dem.inverseLatitude()==false && dem.inverseLongitude()==true) {
            dem = new DemSplitterSW(dem);

        } else {
            dem =  new DemSplitterNW(dem);
        }

        return dem;
    }


    public DemSplitter(DemProvider p) {
        final DemDimension pdim=p.getDim();

        parent=p;
        parent_dim=pdim.DIM;

        cellsize=parent.getCellsize()/2;
        _dim=new DemDimension(
                pdim.DIM*2,
                pdim.OFFSET*3); // Add extra offset (1x) for MultiCell. Original (parent) offset (2x) is used by DemSplitter.

        dim = _dim.DIM;
    }

    public short getElevation(final int index) {
        final int row = index / dim;
        final int col = index % dim;

        final int parent_row=row/2;
        final int parent_col=col/2;

        final int parent_index=parent_row*parent_dim + parent_col;

        final int row_mode=row % 2;
        final int col_mode=col % 2;



        /*
          Kernel:
           a b c
           d E f
           g h i


          Splitted E:
            A B
            C D
         */

        final int e = parent_index;
        final int b = e - parent_dim;
        final int a = b - 1;
        final int c = b + 1;
        final int d = e - 1;
        final int f = e + 1;
        final int h = e + parent_dim;
        final int g = h - 1;
        final int i = h + 1;


        int sum = parent.getElevation(e)*2;
        final float div=12;

        if (row_mode+col_mode == 0) { // A
            sum = sum +
                    parent.getElevation(a)*2 +
                    parent.getElevation(b)*2 +
                    parent.getElevation(c) +
                    parent.getElevation(d)*2 +
                    parent.getElevation(f) +
                    parent.getElevation(g) +
                    parent.getElevation(h);// +
                    //parent.getElevation(i);

        } else if (row_mode==0) {    // B
            sum = sum +
                    parent.getElevation(a) +
                    parent.getElevation(b)*2 +
                    parent.getElevation(c)*2 +
                    parent.getElevation(d) +
                    parent.getElevation(f)*2 +
                    //parent.getElevation(g) +
                    parent.getElevation(h) +
                    parent.getElevation(i);

        } else if (col_mode==0) {    // C
            sum = sum +
                    parent.getElevation(a) +
                    parent.getElevation(b) +
                    //parent.getElevation(c) +
                    parent.getElevation(d)*2 +
                    parent.getElevation(f) +
                    parent.getElevation(g)*2 +
                    parent.getElevation(h)*2 +
                    parent.getElevation(i);

        } else {                     // D
            sum = sum +
                    //parent.getElevation(a) +
                    parent.getElevation(b) +
                    parent.getElevation(c) +
                    parent.getElevation(d) +
                    parent.getElevation(f)*2 +
                    parent.getElevation(g) +
                    parent.getElevation(h)*2 +
                    parent.getElevation(i)*2;
        }

        return (short)Math.round(sum / div);
    }


    @Override
    public DemDimension getDim() {
        return _dim;
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
