package ch.bailu.aat.services.dem.tile;

public class MultiCell4NE extends MultiCell {
    /**
     *      a  b
     *      C  d
     */

    private short a, b, c, d;
    private int dzx, dzy;

    private final DemProvider demtile;
    private final int dim;
    private final int total_cellsize;


    public MultiCell4NE(final DemProvider dem) {
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
        final int a=x-dim;
        final int b=a+1;
        final int c=x;
        final int d=x+1;
        this.a=demtile.getElevation(a);
        this.b=demtile.getElevation(b);
        this.c=demtile.getElevation(c);
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