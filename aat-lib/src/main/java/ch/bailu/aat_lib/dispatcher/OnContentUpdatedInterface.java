package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface OnContentUpdatedInterface {
    OnContentUpdatedInterface NULL = (iid, info) -> {

    };

    void onContentUpdated(int iid, @Nonnull GpxInformation info);
}
