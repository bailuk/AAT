package ch.bailu.aat.services.dem.tile;

public class MultiCell8 extends MultiCell {
    /**
     *      a  b  c
     *      d [e] f
     *      g  h  i
     *      
     *      only works with double offset mode
     */

    private int A,B,C,D,F,G,H,I;
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

        A=demtile.getElevation(a);
        B=demtile.getElevation(b);
        C=demtile.getElevation(c);
        D=demtile.getElevation(d);
        F=demtile.getElevation(f);
        G=demtile.getElevation(g);
        H=demtile.getElevation(h);
        I=demtile.getElevation(i);
        
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
        final int sum = (C + 2*F + I) - (A + 2*D + G); 
        return  (sum * 100)  / total_cellsize;
    }
    
    private int _delta_zy() {
        final int sum = (G + 2*H + I) - (A + 2*B + C); 
        return (sum *100)  / total_cellsize;
    }
}
