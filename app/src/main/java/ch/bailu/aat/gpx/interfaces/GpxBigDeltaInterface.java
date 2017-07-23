package ch.bailu.aat.gpx.interfaces;



public interface GpxBigDeltaInterface extends GpxDeltaInterface  {


    float getMaximumSpeed();
    long getPause();
    long getAutoPause();
    short getAscend();
    short getDescend();
    long getStartTime();
    long getEndTime();

    int getType();
}
