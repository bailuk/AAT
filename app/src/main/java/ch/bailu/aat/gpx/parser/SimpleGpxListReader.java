package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.services.background.ThreadControl;
import ch.bailu.simpleparser.AbsAccess;


public class SimpleGpxListReader extends GpxListReader {

    private final static ThreadControl tc = new ThreadControl() {
        @Override
        public boolean canContinue() {
            return true;
        }
    };


    public SimpleGpxListReader(AbsAccess f) throws IOException {
        super(tc, f);
    }
}
