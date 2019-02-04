package ch.bailu.aat.gpx.interfaces;


import ch.bailu.aat.gpx.attributes.GpxAttributes;

public interface GpxBigDeltaInterface extends GpxDeltaInterface  {


    long getPause();
    long getStartTime();
    long getEndTime();

    GpxType getType();

    GpxAttributes getAttributes();
}
