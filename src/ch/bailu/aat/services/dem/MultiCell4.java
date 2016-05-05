package ch.bailu.aat.services.dem;


public class MultiCell4 extends MultiCell {
    /**
     *      a  b
     *      c  d
     */

    private short a, b, c, d;
    private int dzx, dzy;

    private final DemProvider demtile;
    private final int dim;
    private final int total_cellsize;


    public MultiCell4(final DemProvider dem) {
        demtile = dem;
        dim = dem.getDim().DIM;
        total_cellsize=Math.round(dem.getCellsize()*4f);
    }

    @Override
    public void set(int x) {
            _set(x);
        dzx=_delta_zx();
        dzy=_delta_zy();
    }


    private void _set(int x) {
            /**
             *       a b
             *       x d
             */
            final int a=x-dim;    
            final int b=a+1;
            final int d=x+1;
            this.a=demtile.getElevation(a);
            this.b=demtile.getElevation(b);
            this.c=demtile.getElevation(x);
            this.d=demtile.getElevation(d);
    }
    

    @Override
    public int delta_zx() {
        return dzx;
    }

    @Override
    public int delta_zy() {
        return dzy;
    }

    private int _delta_zx() {
        final int sum = ((b + d) - (a + c)); 
        return  (sum * 100) / total_cellsize;
    }

    private int _delta_zy() {
        final int sum = ((c + d) - (b + a)); 
        return (sum * 100)  / total_cellsize;
    }
}
