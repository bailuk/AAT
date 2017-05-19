package ch.bailu.aat.gpx.parser.state;

import java.io.IOException;

import ch.bailu.aat.gpx.parser.scanner.Scanner;


public class StateXml extends State {

    private final State stateGpx = new StateGpx();
    private final State stateOsm = new StateOsm();
    private final State stateNominatim = new StateNominatim();

    @Override
    public void parse(Scanner io) throws IOException {


        while(true) {
            io.stream.to('<');
            io.stream.read();

            if (io.stream.haveEOF()) {
                //AppLog.d(this, "EOF");
                break;

            } else 	if (io.stream.haveA('g')) { // <gpx
                //AppLog.d(this, "gpx");
                stateGpx.parse(io);
                break;

            } else if (io.stream.haveA('o')) { // <osm
                //AppLog.d(this, "osm");
                stateOsm.parse(io);
                break;

            } else if (io.stream.haveA('s')) { //<searchresults
                stateNominatim.parse(io);
                break;
            }
        }
    }

}
