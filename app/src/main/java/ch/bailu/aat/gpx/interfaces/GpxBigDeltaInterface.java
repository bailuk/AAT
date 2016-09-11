package ch.bailu.aat.gpx.interfaces;



public interface GpxBigDeltaInterface extends GpxDeltaInterface, GpxType  {


    public float getMaximumSpeed();
    public long getPause();
    public long getStartTime();
    public long getEndTime();

    public int getType();
}
