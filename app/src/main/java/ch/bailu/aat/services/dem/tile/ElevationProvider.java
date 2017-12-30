package ch.bailu.aat.services.dem.tile;



public interface ElevationProvider {
    int NULL_ALTITUDE=0;

    short getElevation(int laE6, int loE6);
}
