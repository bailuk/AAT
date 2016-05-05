package ch.bailu.aat.services.dem;

public class MultiCell8 extends MultiCell {
    /**
     *      a  b  c
     *      d [e] f
     *      g  h  i
     */

    private short a,b,c,d,f,g,h,i;
    private int dzx, dzy;

    private final DemProvider demtile;
    private final int dim;
    private final int total_cellsize;
    
    public MultiCell8(final DemProvider dem) {
        demtile=dem;
        dim = dem.getDim().DIM;
        total_cellsize=Math.round(dem.getCellsize()*8f);
    }

    public void set(final int e) {
        final int f=e+1;
        final int h=e+dim;
        final int i=h+1;
        final int g=h-1;

        final int d=e-1;
        final int b=e-dim;
        final int c=b+1;
        final int a=b-1;

        this.a=demtile.getElevation(a);
        this.b=demtile.getElevation(b);
        this.c=demtile.getElevation(c);
        this.d=demtile.getElevation(d);
        this.f=demtile.getElevation(f);
        this.g=demtile.getElevation(g);
        this.h=demtile.getElevation(h);
        this.i=demtile.getElevation(i);
        
        dzx=_delta_zx();
        dzy=_delta_zy();
    }

    public int delta_zx() {
        return dzx;
    }
    
    
    public int delta_zy() {
        return dzy;
    }

    private int _delta_zx() {
        final int sum = (c + 2*f + i) - (a + 2*d + g); 
        return  sum * 100  / total_cellsize;
    }
    
    private int _delta_zy() {
        final int sum = (g + 2*h + i) - (a + 2*b + c); 
        return sum *100  / total_cellsize;
    }
}
