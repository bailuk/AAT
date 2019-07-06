package ch.bailu.aat.services.dem.tile;

public class Dem3Array {
    private static final int DEM3_BUFFER_DIM=1201;
    private static final int DEM3_BUFFER_OFFSET=1;
    public static final DemDimension DIMENSION=
            new DemDimension(DEM3_BUFFER_DIM, DEM3_BUFFER_OFFSET);

    protected final byte[] data = new byte[DEM3_BUFFER_DIM * DEM3_BUFFER_DIM * 2];
    private final DemGeoToIndex toIndex = new DemGeoToIndex(DIMENSION);




    public DemDimension getDim() {
        return DIMENSION;
    }


    public short getElevation(int laE6, int loE6) {
        return getElevation(toIndex.toPos(laE6, loE6));
    }

    public short getElevation(int index) {
        index = index *2;

        final short x = (short) ((data[index] << 8) | (data[index+1]&0xFF));
        return x;
    }

}
