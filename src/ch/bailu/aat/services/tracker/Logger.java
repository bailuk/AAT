package ch.bailu.aat.services.tracker;

import java.io.IOException;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat.helpers.CleanUp;

public class Logger extends GpxInformation implements CleanUp {
    private int state=ID.STATE_WAIT;
    
    public static Logger createNullLogger() {
        return new Logger();
    }
    
    public void logPause() throws IOException {}
    public void log(GpxPointInterface tp) throws IOException {}
    
    @Override
    public void cleanUp() {}
    

    @Override
    public int getID() {
        return ID.INFO_ID_TRACKER;
    }

    public void setState(int s) {
        state = s;
    }

    @Override
    public int getState() {
        return state;
    }


}
