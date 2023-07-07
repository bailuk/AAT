package ch.bailu.aat_lib.gpx.attributes;

import ch.bailu.aat_lib.gpx.GpxInformation;

public final class SensorInformation extends GpxInformation {
        private final GpxAttributes attributes;
        private final long timeStamp = System.currentTimeMillis();


        public SensorInformation(GpxAttributes a) {
            attributes = a;
        }

        @Override
        public GpxAttributes getAttributes() {
            return attributes;
        }

        @Override
        public long getTimeStamp() {
            return timeStamp;
        }
    }

