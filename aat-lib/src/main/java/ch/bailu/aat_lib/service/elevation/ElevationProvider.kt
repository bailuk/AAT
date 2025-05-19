package ch.bailu.aat_lib.service.elevation;

public interface ElevationProvider {
    int NULL_ALTITUDE=0;

    short getElevation(int laE6, int loE6);
}
