package ch.bailu.aat.services.sensor;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxInformation;

public interface SensorInterface {
    GpxInformation getInformation(int iid);

    boolean isConnectionEstablished();

    String getAddress();
    String getName();

    void close();
}
