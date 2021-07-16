package ch.bailu.aat.dispatcher;

import ch.bailu.aat.gpx.GpxInformation;


public interface OnContentUpdatedInterface {
    OnContentUpdatedInterface NULL = (iid, info) -> {

    };

    void onContentUpdated(int iid, GpxInformation info);
}
