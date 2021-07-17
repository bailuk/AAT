package ch.bailu.aat.services.elevation.tile;

public final class DemGeoToIndex {
    private final int DOFFSET;
    private final int LAST_INDEX;
    private final DemDimension dim;


    public DemGeoToIndex(DemDimension _dim) {
        this(_dim, false);
    }

    public DemGeoToIndex(DemDimension _dim, boolean doffset) {
        dim = _dim;

        if (doffset) {
            DOFFSET=dim.OFFSET;
            LAST_INDEX=dim.DIM-1-dim.OFFSET-DOFFSET;
        } else {
            DOFFSET=0;
            LAST_INDEX=dim.DIM-1-dim.OFFSET;
        }
    }


    public boolean hasDoubleOffset() {
        return DOFFSET != 0;
    }

    public int toPos(int laE6, int loE6) {
        final int x=toXPos(loE6);
        final int y=toYPos(laE6);

        return (y*dim.DIM + x);
    }


    public int toXPos(int loE6) {
        if (loE6<0) return dim.OFFSET + inverse(toPos(loE6));
        return DOFFSET + toPos(loE6);
    }


    public int toYPos(int laE6) {
        if (laE6 >0)
            return dim.OFFSET + inverse(toPos(laE6)); // offset is in first row (index starts with 1)
        return DOFFSET + toPos(laE6); // offset is in last column (index starts with 0)
    }


    private int toPos(int cE6) {
        double c = Math.abs(cE6);

        c = c / 1e6d;

        final double deg = (int)c;

        final double min  = (c-deg);
        final double x = min * LAST_INDEX; // we use last index!!!

        return (int)Math.round(x);
    }

    private int inverse(int v) {
        return LAST_INDEX-v;
    }

}
