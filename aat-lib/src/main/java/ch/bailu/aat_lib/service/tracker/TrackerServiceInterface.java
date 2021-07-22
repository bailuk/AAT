package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface TrackerServiceInterface extends StateInterface {
    GpxInformation getLoggerInformation();

    int getPresetIndex();
}
