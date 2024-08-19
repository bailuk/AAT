package ch.bailu.aat_lib.service.tracker;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;

public abstract class Logger extends GpxInformation implements Closeable {
    private int state = StateID.OFF;

    public static final Logger NULL_LOGGER = new Logger() {
        @Override
        public void log(GpxPointInterface tp, GpxAttributes attr) {}
    };

    public void logPause() {
    }

    public abstract void log(GpxPointInterface tp, GpxAttributes attr) throws IOException;

    @Override
    public void close() {
    }

    public void setState(int s) {
        state = s;
    }

    @Override
    public int getState() {
        return state;
    }
}
