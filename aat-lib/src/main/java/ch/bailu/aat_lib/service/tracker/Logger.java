package ch.bailu.aat_lib.service.tracker;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;

public class Logger extends GpxInformation implements Closeable {
    private int state = StateID.OFF;

    public static Logger createNullLogger() {
        return new Logger();
    }

    public void logPause() {
    }

    public void log(GpxPointInterface tp, GpxAttributes attr) throws IOException {
    }

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
