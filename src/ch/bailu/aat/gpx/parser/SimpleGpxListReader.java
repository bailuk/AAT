package ch.bailu.aat.gpx.parser;

import java.io.IOException;

import ch.bailu.aat.helpers.file.AbsContentAccess;
import ch.bailu.aat.services.background.ThreadControl;


public class SimpleGpxListReader extends GpxListReader {

    private final static ThreadControl tc = new ThreadControl() {
        @Override
        public boolean canContinue() {
            return true;
        }
    };


    public SimpleGpxListReader(AbsContentAccess f) throws IOException {
        super(tc, f);
    }
}
