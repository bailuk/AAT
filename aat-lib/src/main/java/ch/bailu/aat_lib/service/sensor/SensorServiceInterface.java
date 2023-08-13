package ch.bailu.aat_lib.service.sensor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface SensorServiceInterface {
    @Nullable GpxInformation getInformationOrNull(int infoID);

    @Nonnull GpxInformation getInfo(int iid);

    void updateConnections();

    void scan();
}
