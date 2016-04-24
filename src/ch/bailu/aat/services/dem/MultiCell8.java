package ch.bailu.aat.services.dem;

public class MultiCell8 {
    /**
     *      a  b  c
     *      d [e] f
     *      g  h  i
     */

    private short a,b,c,d,f,g,h,i;
    private double dzx, dzy;

    private final DemProvider demtile;
    private final int dim;
    private final int cellsize;
    private final int total_cellsize;
    
    public MultiCell8(final DemProvider dem) {
        demtile=dem;
        dim = dem.getDim().DIM;
        cellsize=dem.getCellsize();
        total_cellsize=cellsize*8;
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

    public double delta_zx() {
        return dzx;
    }
    
    
    public double delta_zy() {
        return dzy;
    }
    
    private double _delta_zx() {
        final double sum = (c + 2*f + i) - (a + 2*d + g); 
        return  sum / total_cellsize;
    }
    
    private double _delta_zy() {
        final double sum = (g + 2*h + i) - (a + 2*b + c); 
        return sum  / total_cellsize;
    }
}
