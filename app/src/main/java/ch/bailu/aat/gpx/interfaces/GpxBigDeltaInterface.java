package ch.bailu.aat.gpx.interfaces;



public interface GpxBigDeltaInterface extends GpxDeltaInterface  {


    float getMaximumSpeed();
    long getPause();
    long getAutoPause();
    long getStartTime();
    long getEndTime();

    int getType();
}
