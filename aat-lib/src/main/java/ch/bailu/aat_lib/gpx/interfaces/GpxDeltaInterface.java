package ch.bailu.aat_lib.gpx.interfaces;


import ch.bailu.aat_lib.coordinates.BoundingBoxE6;

public interface GpxDeltaInterface {
    float getDistance();
    float getSpeed();
    float getAcceleration();
    long  getTimeDelta();
    BoundingBoxE6 getBoundingBox();
}
