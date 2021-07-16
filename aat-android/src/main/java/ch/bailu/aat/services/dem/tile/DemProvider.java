package ch.bailu.aat.services.dem.tile;

public interface DemProvider {


    short getElevation(int index);
    DemDimension getDim();

    float getCellsize();

    boolean inverseLatitude();
    boolean inverseLongitude();


    DemProvider NULL = new DemProvider() {

        @Override
        public short getElevation(int index) {
            return 0;
        }

        @Override
        public DemDimension getDim() {
            return Dem3Array.DIMENSION;
        }

        @Override
        public float getCellsize() {
            return 50;
        }

        @Override
        public boolean inverseLatitude() {
            return false;
        }

        @Override
        public boolean inverseLongitude() {
            return false;
        }

    };
}
