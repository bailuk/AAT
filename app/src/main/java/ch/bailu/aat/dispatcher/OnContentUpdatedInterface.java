package ch.bailu.aat.dispatcher;

import ch.bailu.aat.gpx.GpxInformation;


public interface OnContentUpdatedInterface {
    OnContentUpdatedInterface NULL = new OnContentUpdatedInterface() {
        @Override
        public void onContentUpdated(int iid, GpxInformation info) {

        }
    };

    void onContentUpdated(int iid, GpxInformation info);
}
