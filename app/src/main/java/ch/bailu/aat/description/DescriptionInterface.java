package ch.bailu.aat.description;

import ch.bailu.aat.gpx.GpxInformation;


public interface DescriptionInterface {
    static final DescriptionInterface NULL = new DescriptionInterface() {
        @Override
        public void updateGpxContent(GpxInformation info) {

        }
    };

    void updateGpxContent(GpxInformation info);
}
