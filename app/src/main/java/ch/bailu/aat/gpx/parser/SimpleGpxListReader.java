package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.helpers.file.AbsAccess;
import ch.bailu.aat.services.background.ThreadControl;


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
