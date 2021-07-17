package ch.bailu.aat.services.sensor;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface SensorInterface {
    GpxInformation getInformation(int iid);

    String getName();

    void close();
}
