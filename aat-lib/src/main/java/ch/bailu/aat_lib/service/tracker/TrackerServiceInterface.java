package ch.bailu.aat_lib.service.tracker;

import ch.bailu.aat_lib.gpx.GpxInformationProvider;

public interface TrackerServiceInterface extends StateInterface, GpxInformationProvider {
    int getPresetIndex();
}
