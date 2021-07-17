package ch.bailu.aat_lib.service.location;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface LocationServiceInterface {
    void setPresetIndex(int presetIndex);

    boolean isMissingUpdates();

    boolean isAutopaused();

    boolean hasLoggableLocation(GpxInformation location);

    GpxInformation getLoggableLocation();

    GpxInformation getLocationInformation();
}
