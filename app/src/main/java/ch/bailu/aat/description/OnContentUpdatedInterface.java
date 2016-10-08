package ch.bailu.aat.description;

import ch.bailu.aat.gpx.GpxInformation;


public interface OnContentUpdatedInterface {
    static final OnContentUpdatedInterface NULL = new OnContentUpdatedInterface() {
        @Override
        public void updateGpxContent(GpxInformation info) {

        }
    };

    void updateGpxContent(GpxInformation info);
}
