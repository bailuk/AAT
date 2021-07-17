package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.gpx.GpxInformation;

public interface OnContentUpdatedInterface {
    OnContentUpdatedInterface NULL = (iid, info) -> {

    };

    void onContentUpdated(int iid, GpxInformation info);
}
