package ch.bailu.aat.services.sensor;

import ch.bailu.aat.gpx.GpxInformation;

public interface SensorInterface {
    GpxInformation getInformation(int iid);

    String getName();

    void close();
}
