package ch.bailu.aat_lib.gpx.interfaces;


import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;

public interface GpxBigDeltaInterface extends GpxDeltaInterface  {


    long getPause();
    long getStartTime();
    long getEndTime();

    GpxType getType();

    GpxAttributes getAttributes();
}