package ch.bailu.aat.gpx.parser;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.services.background.ThreadControl;


public class SimpleGpxListReader extends GpxListReader {

    private final static ThreadControl tc = new ThreadControl() {
        @Override
        public boolean canContinue() {
            return true;
        }
    };


    public SimpleGpxListReader(File f) throws IOException {
        super(tc, f);
    }
}
