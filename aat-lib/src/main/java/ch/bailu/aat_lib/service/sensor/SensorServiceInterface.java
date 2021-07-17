package ch.bailu.aat_lib.service.sensor;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface SensorServiceInterface {
    GpxInformation getInformationOrNull(int infoID);

    GpxInformation getInformation(int iid);

    void updateConnections();

    void scan();
}
