package ch.bailu.aat.services.sensor;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxInformation;

public interface SensorInterface extends Closeable {
    boolean isValid();
    GpxInformation getInformation(int iid);
}
