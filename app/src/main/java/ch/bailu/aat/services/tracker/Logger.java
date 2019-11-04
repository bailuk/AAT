package ch.bailu.aat.services.tracker;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;

public class Logger extends GpxInformation implements Closeable {
    private int state = StateID.OFF;

    public static Logger createNullLogger() {
        return new Logger();
    }

    public void logPause() throws IOException {}
    public void log(GpxPointInterface tp, GpxAttributes attr) throws IOException {}

    @Override
    public void close() {}

    public void setState(int s) {
        state = s;
    }

    @Override
    public int getState() {
        return state;
    }


}
