package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface TrackerServiceInterface {
    GpxInformation getLoggerInformation();
    State getState();

    int getPresetIndex();
}
