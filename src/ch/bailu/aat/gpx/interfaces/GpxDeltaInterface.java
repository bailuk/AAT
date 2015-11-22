package ch.bailu.aat.gpx.interfaces;

import ch.bailu.aat.coordinates.BoundingBox;

public interface GpxDeltaInterface {
    double getBearing();
    float getDistance();
    float getSpeed();
    float getAcceleration();
    long  getTimeDelta();
    public BoundingBox getBoundingBox();
}
